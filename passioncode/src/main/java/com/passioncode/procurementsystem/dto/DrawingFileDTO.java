package com.passioncode.procurementsystem.dto;

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
public class DrawingFileDTO {
	
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
     * uuid + fileName 합친거!
     */
    private String uuidAndFileName;

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

	public DrawingFileDTO(String drawingFile) {
		super();
		String uploadPath = drawingFile.substring(0,20);
		//log.info("어디어디 보자~~~~~~~ : "+uploadPath);
		// \PassionCode\ upload\
		String folderPath = drawingFile.substring(20,31);
		//log.info("어디어디 보자2~~~~~~~ : "+folderPath);
		// 2023\06\21\
		String uuidAndFileName = drawingFile.substring(31);
		//log.info("어디어디 보자3~~~~~~~ : "+uuidAndFileName);
		// 97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg
		// thumb_97743ec3-da5b-44a3-9e79-c98e4faf90b3_HappyDay!!!!.jpg
		
		this.folderPath = folderPath;
		this.uuidAndFileName = uuidAndFileName;
	}

}
