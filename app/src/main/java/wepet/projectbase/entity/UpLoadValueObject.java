package wepet.projectbase.entity;

import java.io.File;

/**
 * Created by ccei on 2016-08-13.
 */
public class UpLoadValueObject {

    public File file; //업로드할 파일
    public boolean tempFiles; //임시파일 유무

    public UpLoadValueObject(File file, boolean tempFiles) {
        this.file = file;
        this.tempFiles = tempFiles;
    }
}
