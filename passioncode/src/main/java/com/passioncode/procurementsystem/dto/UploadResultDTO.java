package com.passioncode.procurementsystem.dto;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 파일업로드(도면,계약서)를 위한 파일업로드DTO
 * @author KSH
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadResultDTO {
	/**
	 * 파일이름
	 */
	private String fileName;
	
	/**
	 * uuid
	 */
    private String uuid;
    
    /**
     * 파일경로
     */
    private String folderPath;
 
    public String getImageURL(){
        try {
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(folderPath+"/thumb_"+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    
    

}
