package com.passioncode.procurementsystem.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.passioncode.procurementsystem.dto.DrawingFileDTO;
import com.passioncode.procurementsystem.dto.UploadResultDTO;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@RestController
@RequestMapping("procurement1")
@Log4j2
public class PS1UploadRestController {

	@Value("${com.passioncode.procurementsystem.drawingFile.upload.path}")    //리소스의 설정 파일 내용(값)가져와서 셋팅
    //application.properties 에서 com.passioncode.procurementsystem.drawingFile.upload.path = /PassionCode/upload/drawing/ 값을 읽어오는 방법
    //이렇게 설정을해야 경로가 바꼈을때 리소스에서 가서 고쳐주기만 하면 됨
    private String drawingUploadPath;
	
	@Value("${com.passioncode.procurementsystem.contract.upload.path}")    //리소스의 설정 파일 내용(값)가져와서 셋팅
	//application.properties 에서 com.passioncode.procurementsystem.contract.upload.path = /PassionCode/upload/contract/ 값을 읽어오는 방법
	//이렇게 설정을해야 경로가 바꼈을때 리소스에서 가서 고쳐주기만 하면 됨
	private String contractUploadPath;
		
	/**
     * 오늘날짜로 도면 폴더만들기
     * @return 만든폴더이름 리턴(ex>2023/04/05)
     */
    private String makeDrawingFolder() {

        //String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        //2023/04/25   -> 폴더로 하려면 윈도우는 \ 이렇게 구분자 
        //String folderPath =  str.replace("//", File.separator);  //   "//" 여기서는 //두개를 뜻함  
    	//\ 순수한 문자를 의미한다! 
    	//윈도우 \ 이걸로 구분하기 때문에 \\ 이렇게 해야 했음  c:\\abc\\abc 
    	//근데 여기서는 필요 없음
        //요즘엔 자동으로 구분해줘서 위에처럼 안해줘도 됨!
    	
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        log.info("폴더 만드는 함수 안에서 folderPath 값 세팅좀 보자!"+folderPath);

        // make folder --------
        File uploadPathFolder = new File(drawingUploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {  //파일이 존재하지 않으면 만들라!
            uploadPathFolder.mkdirs();  //디렉토리 만들기
        }
        return folderPath;
    }
    
	/**
     * 오늘날짜로 계약서 폴더만들기
     * @return 만든폴더이름 리턴(ex>2023/04/05)
     */
    private String makeContractFolder() {
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        // make folder --------
        File uploadPathFolder = new File(contractUploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {  //파일이 존재하지 않으면 만들라!
            uploadPathFolder.mkdirs();  //디렉토리 만들기
        }
        return folderPath;
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
  			log.info("파일 종류는 : "+contentType);
  				return contentType.startsWith("image"); 				//image 글자로 시작하면 true 리턴
  		} catch (IOException e) {   									//IO 이셉션으로 null 말고, 이미지파일 분석일때만 오류일때만 찍고
  			log.info("이미지 파일 분석 오류");    						//시스템이나 파일시스템(파일이 깨질때) 즉, 이미지파일일때 깨지면 발생
  			e.printStackTrace();
  		} catch (Exception e) {
  			log.info("null 값! 없는 파일 종류~!!");
  		}
  		return false;
  	}
	
    /**
     * 파일업로드 하기(원본과 썸네일 저장) <br>
     * 품목정보 등록->도면, 계약 등록->계약서
     * @param uploadFile
     * @return
     */
    @PostMapping("/drawing/uploadAjax")
    public ResponseEntity<DrawingFileDTO> uploadFile(MultipartFile uploadFile){ 		//ResponseEntity 그 응답의 여러가지 영역을 바꾸고 싶을때 붙이는거
    	
    	log.info("일단 업로드파일 어떻게 읽나 보자 : "+uploadFile);
    	    	
    	//실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로  
        //String originalName = uploadFile.getOriginalFilename();
        //log.info("파일이름 edge 흠 어디 한번보자!(edge는 사실상 chrome이다) : "+originalName);
        //String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        //edge에서 파일이름 요즘엔 바로 읽어와서 파일네임 처리 위에처럼 안해도 된다!    
        
        //IE도 지원 종료되고, Edge도 이제는 크롬이니까, 파일이름 바로 읽어도 된다!
        String fileName = uploadFile.getOriginalFilename();

        log.info("fileName: " + fileName);
        //날짜 폴더 생성
        String folderPath = makeDrawingFolder();
        log.info("현재 메인 페스 drawingUploadPath 이거 봐보자 : "+drawingUploadPath);
        log.info("어떻게 folderPath 세팅되나 보자!!!! : "+folderPath);
        
        //UUID
        String uuid = UUID.randomUUID().toString();

        //저장할 파일 이름 중간에 "_"를 이용해서 구분
//        String saveName = drawingUploadPath + File.separator + folderPath + File.separator + uuid +"_" + fileName;
        String saveName = drawingUploadPath + folderPath + uuid +"_" + fileName;
        log.info("saveName 이름좀 봐보자 : "+saveName);
        Path savePath = Paths.get(saveName);
        log.info("savePath 이거 페스이름 봐보자 : "+savePath);
//        log.info("savePath.toFile() 이거 한번 봐보자 : "+savePath.toFile());
//        log.info("savePath.getFileName() 이거 한번 봐보자 : "+savePath.getFileName());
//        log.info("savePath.getParent() 이거 한번 봐보자 : "+savePath.getParent());
//        log.info("savePath.getRoot() 이거 한번 봐보자 : "+savePath.getRoot());
        
        DrawingFileDTO drawingFileDTO = new DrawingFileDTO();
        
        File originFile = new File(saveName);
        log.info("만들어진 File을 봐보자 : "+originFile);
        log.info("이미지 파일 체크 여부 한번 보자 : "+checkImageType(originFile));
        boolean isImage = checkImageType(originFile);
        
        try {
            //원본 파일 저장
            uploadFile.transferTo(savePath);
            
            //올린 파일이 이미지 파일이 있는지 검사 -> 이미지 파일 일때만 썸네일 파일 만들자
            if(isImage) {
            	//섬네일 생성
//            	String thumbnailSaveName = drawingUploadPath + File.separator + folderPath + File.separator +"thumb_" + uuid +"_" + fileName;
            	String thumbnailSaveName = drawingUploadPath + folderPath +"thumb_" + uuid +"_" + fileName;
            	//섬네일 파일 이름은 중간에 thumb_로 시작하도록
            	File thumbnailFile = new File(thumbnailSaveName);
            	//섬네일 생성
            	Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile,100,100 );  //Thumbnailator라이브러리 그리들에 추가
            	
            }
            	
            drawingFileDTO = DrawingFileDTO.builder().fileName(fileName).uuid(uuid).folderPath(folderPath).image(isImage).uuidAndFileName(uuid+fileName).build();
            log.info("업로드 DTO 에 넣은거 보자 : "+drawingFileDTO);
            log.info("어디 DTO의 파일 이름 읽어보자 : ", drawingFileDTO.getDrawingFile());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
        return new ResponseEntity<>(drawingFileDTO, HttpStatus.OK);
    }
    
    /**
     * 업로드파일 보여주기(원본,썸네일) <br>
     * thumbnailURL -> 썸네일 파일 <br>
     * imageURL, thumbnailURL & size=1 -> 원본 파일 <br>
     * @param fileName
     * @param size
     * @return
     */
    @GetMapping("/drawing/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
    	/*
    	//원본파일
    	divArea.append("<img src='/procurement1/display?fileName="+dto.thumbnailURL+" &size=1' >");
    	divArea.append("<img src='/procurement1/display?fileName="+dto.imageURL+" ' >");
    	//썸네일파일
    	divArea.append("<img src='/procurement1/display?fileName="+dto.thumbnailURL+"'>");
    	*/
    	log.info("일단 display get매핑 했고, fileName 읽어보자 : "+fileName);
        ResponseEntity<byte[]> result = null;

        try {
        	//이미 위에서 받아서 올때, 디코딩을 해서 오기 때문에, 또 디코딩 처리를 해줄 필요가 없다!
        	//특히 + 같은경우는 이미 해줬는데, 또 디코딩 해주면, +가 사라진다! 그러니까, 그냥 받아온 파일이름 그대로 쓰면된다!
            //String srcFileName =  URLDecoder.decode(fileName,"UTF-8");
            String srcFileName =  fileName;
            log.info("fileName: " + srcFileName);

//            File file = new File(drawingUploadPath +File.separator+ srcFileName);
            File file = new File(drawingUploadPath + srcFileName);
            log.info("file.getParent() 읽어보자 : "+file.getParent());
            
            //thumbnailURL & size=1 -> 원본 파일
            if(size != null && size.equals("1")){
                file  = new File(file.getParent(), file.getName().substring(6));
            }

            log.info("file: " + file);
            
//            log.info("파일을 문자로 읽어보자 : "+file.toString());
//            String test = URLDecoder.decode(file.toString(),"UTF-8");
//            log.info("테스트중이야~~ : "+test);
//            File filetest = new File(test);
//            log.info("파일 테스트 중이야~~ : "+filetest);

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            log.info("보내는 타입 보자 : "+Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }
    
    
    @PostMapping("/drawing/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
    	log.info("삭제할때 건내주는 파일이름 좀 보자 : "+fileName);

        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
//            File file = new File(drawingUploadPath +File.separator+ srcFileName);
            File file = new File(drawingUploadPath + srcFileName);
            log.info("파일 봐보자... : "+file);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "thumb_" + file.getName());
            log.info("썸네일 봐보자... : "+thumbnail);
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
}
