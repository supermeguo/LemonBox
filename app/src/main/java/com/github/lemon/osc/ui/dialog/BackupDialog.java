package com.github.lemon.osc.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.lemon.osc.R;
import com.github.lemon.osc.base.App;
import com.github.lemon.osc.data.AppDataManager;
import com.github.lemon.osc.ui.activity.HomeActivity;
import com.github.lemon.osc.ui.adapter.BackupAdapter;
import com.github.lemon.osc.util.DefaultConfig;
import com.github.lemon.osc.util.FileUtils;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BackupDialog extends BaseDialog {

    public BackupDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_backup);
        TvRecyclerView tvRecyclerView = findViewById(R.id.list);
        BackupAdapter adapter = new BackupAdapter();
        tvRecyclerView.setAdapter(adapter);
        adapter.setNewData(allBackup());
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvName) {
                    restore((String) adapter.getItem(position));
                } else if (view.getId() == R.id.tvDel) {
                    delete((String) adapter.getItem(position));
                    adapter.setNewData(allBackup());
                }
            }
        });
        findViewById(R.id.backupNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup();
                adapter.setNewData(allBackup());
            }
        });
        findViewById(R.id.storagePermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "已获得存储权限", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context,  DefaultConfig.StoragePermissionGroup(), 1);
                }
            }
        });
    }

    List<String> allBackup() {
        ArrayList<String> result = new ArrayList<>();
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(root + "/tvbox_backup/");
            File[] list = file.listFiles();
            Arrays.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) return -1;
                    return o1.isFile() && o2.isDirectory() ? 1 : o2.getName().compareTo(o1.getName());
                }
            });
            if (file.exists()) {
                for (File f : list) {
                    if (result.size() > 10) {
                        FileUtils.recursiveDelete(f);
                        continue;
                    }
                    if (f.isDirectory()) {
                        result.add(f.getName());
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    void restore(String dir) {
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File backup = new File(root + "/tvbox_backup/" + dir);
            if (backup.exists()) {
                File db = new File(backup, "sqlite");
                if (AppDataManager.restore(db)) {
                    byte[] data = FileUtils.readSimple(new File(backup, "hawk"));
                    if (data != null) {
                        String hawkJson = new String(data, "UTF-8");
                        JSONObject jsonObject = new JSONObject(hawkJson);
                        Iterator<String> it = jsonObject.keys();
                        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences("Hawk2", Context.MODE_PRIVATE);
                        while (it.hasNext()) {
                            String key = it.next();
                            String value = jsonObject.getString(key);
                            if (key.equals("cipher_key")) {
                                App.getInstance().getSharedPreferences("crypto.KEY_256", Context.MODE_PRIVATE).edit().putString(key, value).commit();
                            } else {
                                sharedPreferences.edit().putString(key, value).commit();
                            }
                        }
                        Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_rest_ok), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_rest_fail_hk), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_rest_fail_db), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void backup() {
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(root + "/tvbox_backup/");
            if (!file.exists())
                file.mkdirs();
            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            File backup = new File(file, f.format(now));
            backup.mkdirs();
            File db = new File(backup, "sqlite");
            if (AppDataManager.backup(db)) {
                SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences("Hawk2", Context.MODE_PRIVATE);
                JSONObject jsonObject = new JSONObject();
                for (String key : sharedPreferences.getAll().keySet()) {
                    jsonObject.put(key, sharedPreferences.getString(key, ""));
                }
                sharedPreferences = App.getInstance().getSharedPreferences("crypto.KEY_256", Context.MODE_PRIVATE);
                for (String key : sharedPreferences.getAll().keySet()) {
                    jsonObject.put(key, sharedPreferences.getString(key, ""));
                }
                if (!FileUtils.writeSimple(jsonObject.toString().getBytes("UTF-8"), new File(backup, "hawk"))) {
                    backup.delete();
                    Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_bkup_fail_hk), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_bkup_ok), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_bkup_fail_db), Toast.LENGTH_SHORT).show();
                backup.delete();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_bkup_fail), Toast.LENGTH_SHORT).show();
        }
    }
    void delete(String dir) {
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File backup = new File(root + "/tvbox_backup/" + dir);
            FileUtils.recursiveDelete(backup);
            Toast.makeText(getContext(), HomeActivity.getRes().getString(R.string.set_bkup_del), Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}