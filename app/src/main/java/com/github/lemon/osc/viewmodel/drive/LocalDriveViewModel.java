package com.github.lemon.osc.viewmodel.drive;

import com.github.lemon.osc.bean.DriveFolderFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pj567
 * @date :2020/12/18
 * @description:
 */
public class LocalDriveViewModel extends AbstractDriveViewModel {

    @Override
    public String loadData(LoadDataCallback callback) {
        if (currentDriveNote == null)
            currentDriveNote = new DriveFolderFile(null, "", 0, false, null, null);
        String path = currentDrive.name + currentDriveNote.getAccessingPathStr() + currentDriveNote.name;
        if (currentDriveNote.getChildren() == null) {
            File[] files = (new File(path)).listFiles();
            List<DriveFolderFile> items = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    int extNameStartIndex = file.getName().lastIndexOf(".");
                    items.add(new DriveFolderFile(currentDriveNote, file.getName(), 0, file.isFile(),
                            file.isFile() && extNameStartIndex >= 0 && extNameStartIndex < file.getName().length() ?
                                    file.getName().substring(extNameStartIndex + 1) : null,
                            file.lastModified()));
                }
            }
            sortData(items);
            DriveFolderFile backItem = new DriveFolderFile(null, null, 0, false, null, null);
            backItem.parentFolder = backItem;
            items.add(0, backItem);
            currentDriveNote.setChildren(items);
            if (callback != null) {
                callback.callback(currentDriveNote.getChildren(), false);
            }
        } else {
            sortData(currentDriveNote.getChildren());
            callback.callback(currentDriveNote.getChildren(), true);
        }
        return path;
    }

}