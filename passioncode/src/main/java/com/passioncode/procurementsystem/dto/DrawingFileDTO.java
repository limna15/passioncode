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
    private String drawingUploadPath = "/PassionCode/upload/drawing/";
	
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
    
    /**
     * uuid + fileName 합친거!
     */
    private String uuidAndFileName;
       
    public String getImageURL(){
        try {
//            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
            return URLEncoder.encode(folderPath+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try {
//            return URLEncoder.encode(folderPath+"/thumb_"+uuid+"_"+fileName,"UTF-8");
            return URLEncoder.encode(folderPath+"thumb_"+uuid+"_"+fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String getDrawingFile() {    	
//        return drawingUploadPath+"/"+folderPath+"/"+uuid+"_"+fileName;
        return drawingUploadPath+folderPath+uuid+"_"+fileName;
    }
	    
	public DrawingFileDTO(String drawingFile) {
		super();
		//String drawingFile = "\\PassionCode\\upload\\drawing\\2023\\06\\21\\65334b3a-f3c2-4d06-92b4-7850e2ede958_HappyLunch~.jpg";
		String drawingUploadPath = drawingFile.substring(0,28);
		//log.info("어디어디 보자~~~~~~~ : "+uploadPath);
		// \PassionCode\ upload\drawing\
		String folderPath = drawingFile.substring(28,39);
		//log.info("어디어디 보자2~~~~~~~ : "+folderPath);
		// 2023\06\21\
		String uuidAndFileName = drawingFile.substring(39);
		//log.info("어디어디 보자3~~~~~~~ : "+uuidAndFileName);
		// 97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg  -> db에 저장한 도면파일은 thumb는 없으니까 이렇게만 나온다.
		// thumb_97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg
		
		this.folderPath = folderPath;
		this.uuidAndFileName = uuidAndFileName;
		this.drawingUploadPath = drawingUploadPath;
	}
	
	 public String getImageURLByUuidAndFileName(){ 	//imageURLByUuidAndFileName
        try {
            return URLEncoder.encode(folderPath+uuidAndFileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURLByUuidAndFileName(){ 	//thumbnailURLByUuidAndFileName
        try {
            return URLEncoder.encode(folderPath+"thumb_"+uuidAndFileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String getDrawingFileByUuidAndFileName() {    //drawingFileByUuidAndFileName	
        return drawingUploadPath+folderPath+uuidAndFileName;
    }
	
	public boolean isImage() {
		return image;
	}
    

}
