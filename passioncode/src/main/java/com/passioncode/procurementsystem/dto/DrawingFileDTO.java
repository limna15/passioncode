package com.passioncode.procurementsystem.dto;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;

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
public class DrawingFileDTO {
	
	@Builder.Default
    private String drawingUploadPath = "/PassionCode/upload/drawing";
	
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
    
    /**
     * 이미지 여부
     */
    private boolean image;
    
       
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
    
    public String getDrawingFile() {    	
        return drawingUploadPath+"/"+folderPath+"/"+uuid+"_"+fileName;
    }
	
//	public void setDrawingFile(String drawingFile) {
//		this.drawingFile = drawingFile;
//	}
	
	public boolean isImage() {
		return image;
	}
    

}
