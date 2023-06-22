package com.passioncode.procurementsystem.dto;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;

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
    
    /**
     * 이미지파일인지 체크하는 함수 <br>
     * true = 이미지파일, false = 그 외 파일
     * @param file
     * @return
     */
  	private boolean checkImageType(File file) {
  		try {
  			String contentType=Files.probeContentType(file.toPath());  //확장자 명으로 종류파악(그래서 현재는 임의로 확장자 바꾸면 jpg 도 읽어옴)
  			//log.info("파일 종류는 : "+contentType);
  				return contentType.startsWith("image"); 				//image 글자로 시작하면 true 리턴
  		} catch (IOException e) {   									//IO 이셉션으로 null 말고, 이미지파일 분석일때만 오류일때만 찍고
  			//log.info("이미지 파일 분석 오류");    						//시스템이나 파일시스템(파일이 깨질때) 즉, 이미지파일일때 깨지면 발생
  			e.printStackTrace();
  		} catch (Exception e) {
  			//log.info("null 값! 없는 파일 종류~!!");
  		}
  		return false;
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
		String uuid = drawingFile.substring(39, 75);
		//log.info("uuid 보자!! : "+ uuid);
		// 65334b3a-f3c2-4d06-92b4-7850e2ede958
		// 언더바 _ 제외하고 fileName 보자!!
		String fileName = drawingFile.substring(76);
		//log.info("어디 이제 오리지날 파일이름 보자!!! : "+fileName);
		// HappyLunch~.jpg	
		
		File originFile = new File(drawingFile);
		boolean isImage = checkImageType(originFile);
		
		this.drawingUploadPath = drawingUploadPath;
		this.folderPath = folderPath;
		this.uuid = uuid;
		this.fileName = fileName;
		this.image = isImage;
	}
		
	public boolean isImage() {
		return image;
	}
    

}
