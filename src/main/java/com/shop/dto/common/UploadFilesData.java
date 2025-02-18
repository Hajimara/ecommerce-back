package com.shop.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFilesData {

    String fileName;
    String originFileName;
    String objectUrl;
    String datePath;
}