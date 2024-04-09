
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.web.product.DiskFileUpload;
import com.model2.mvc.web.product.FileItem;


public ~~~{

/// file upload
	// enctype = multipart/form-data :: @RequestBody
	public String addProductOld( Model model, HttpServletRequest request) throws Exception {

		
		/// Content-Type이 'multipart/form-data'인 경우
		if(FileUpload.isMultipartContent(request)) {
			
			// file이 임시로 저장될 경로 (relative path도 가능)
			//ServletContext의 getRealPath() :: 주어진 path가 존재한다는 가정 하에(?) 유효한 경로를 알려준다.
			System.out.println("flag :: "+request.getServletContext().getRealPath("/images/uploadFiles"));
			String tempDirectory = request.getServletContext().getRealPath("/images/uploadFiles");  // Tomcat 내 실제 경로에 save해야 eclipse에서 file 갱신 bug 피할 수 있음.
			
			
			/// ServletFileUpload과 DiskFileItemFactory로 class가 분리되었다(?)...
			DiskFileUpload fileUpload = new DiskFileUpload();  // multipart/form-data 내 boundary data를 추상화 캡슐화한 객체
			/*
			 * setRepositoryPath :: threshold 크기를 넘친 경우 file을 임시로 저장해둘 경로 지정
			 * setSizeMax :: 받아들일 수 있는 file의 최대 크기 지정
			 * setSizeThreshold :: disk에 direct로 저장할 threshold 크기 임계치(?) 지정
			 */
			fileUpload.setRepositoryPath(tempDirectory); 
			fileUpload.setSizeMax(unitKB*unitKB*10);  // 10MB
			fileUpload.setSizeThreshold(unitKB * 100);  // 100KB
			
			// input된 file 크기가 최대 file 크기를 넘지 않은 경우
			if(request.getContentLength() < fileUpload.getSizeMax()  ) {
				
				Product tempProduct = new Product();

				List fileItemList = fileUpload.parseRequest(request);  // request body 내 각 boundary를 잘 parsing해서 index로 관리
				int size = fileItemList.size();
				// 순차적으로 각 boundary마다 parsing한 data를 control하고자 함
				for(int i = 0; i<size; i++) {
					
					FileItem fileItem = (FileItem) fileItemList.get(i);
					
					/* 
					 *  isFormField() :: parameter이면 true, file 형식이면 false
					 */
					/// request data가 parameter 형식인 경우
					if( fileItem.isFormField()) {
						
						if(fileItem.getFieldName().equals("manuDate")) {
							StringTokenizer token = null;
							token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String manuDate = token.nextToken() + token.nextToken() + token.nextToken();
							tempProduct.setManuDate(manuDate);
						} else if (fileItem.getFieldName().equals("prodName")) {
							tempProduct.setProdName(fileItem.getString("euc-kr"));
						} else if(fileItem.getFieldName().equals("prodDetail")) {
							tempProduct.setProdDetail(fileItem.getString("euc-kr"));
						} else if (fileItem.getFieldName().equals("price")) {
							tempProduct.setPrice( Integer.parseInt(fileItem.getString("euc-kr")) );
						}
						/// request data가 file 형식인 경우
					} else {
						
						/// binary file이 존재하는 경우
						if(fileItem.getSize() > 0) {
							
							/*
							 *  getName() :: client file system의 원본 filename getter
							 *  근데 실질적으로 우리는 가장 마지막 filename 그 자체만 필요하니까 추가적으로 parsing
							 */
							int index = fileItem.getName().lastIndexOf("\\");
							if(index == -1)
								index = fileItem.getName().lastIndexOf("/");
							
							// String fileName = fileItem.getName().substring(index + 1);  << 동일 image의 경우 random 이름으로 식별성 부여 필요함
							// String extention = "." +  fileItem.getName().substring( index+1 ).split("[.]");  // dot은 정규식에서 임의의 한 문자를 의미하는 특수문자이다... 그래서 특수 정규식으로 따로 표현하여 일반 문자로 취급해야 함.
							String extention = "." +  fileItem.getName().substring( index+1 ).split("\\.")[1];  // 확장자
							String fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;
							tempProduct.setFileName(fileName);  // view 단에서 static하게 경로 잡아주고, name만 변경해주면 됨
							
							try {
								File uploadedFile = new File(tempDirectory, fileName);
								fileItem.write(uploadedFile);  // save file to 'disk'
							} catch(IOException e) {
								e.printStackTrace();
							}
						} else {
							tempProduct.setFileName("images/empty.GIF");  // root 경로는 context root인데, 현재 view에서 '/image/uploadFiles'를 static path로 잡은 상태
						}
					}
				}  /// for end
				
				service.addProduct(tempProduct);
				model.addAttribute("product", tempProduct);
				/// input된 file 크기가 최대 file 크기를 넘어선 경우
			} else {
				
				int overSize = (request.getContentLength() / 1000000);  // 왜 백 만으로 나누지?
				System.out.println("<script>alert('file의 크기는 최대 10MB까지 지원합니다. 현재 upload하신 file의 크기는 '"
						+ overSize +"MB' 입니다.')");
				System.out.println("history.back(); </script>");
			}
			/// Content-Type이 'multipart/form-data'가 아닌 경우


} else {
			System.out.println("Accept할 수 없는 encoding type입니다. 오직 'multipart/form-data'만 허용합니다.");
		}
		
		return "forward:/product/addProduct.jsp";
	}
	
	
public String updateProductOld( Model model, HttpServletRequest request) throws Exception {
		
		if(FileUpload.isMultipartContent(request)) {
			
			String tempPath = request.getServletContext().getRealPath("/images/uploadFiles");
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(tempPath);
			fileUpload.setSizeMax(unitKB * unitKB * 10);
			fileUpload.setSizeThreshold(unitKB * 100);
			
			if(request.getContentLength() < fileUpload.getSizeMax()) {
				
				Product product = new Product();
				
				List fileItemList = fileUpload.parseRequest(request);
				// System.out.println("fileItemList :: " + fileItemList);
				// System.out.println("real path (/images/uploadFiles) :: " + request.getServletContext().getRealPath("/images/uploadFiles"));
				int size = fileItemList.size();
				for(int i = 0; i < size; i++) {

					FileItem fileItem = (FileItem) fileItemList.get(i);
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("manuDate")) {
							StringTokenizer token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String manuDate = "";
							while(token.hasMoreTokens()) 
								manuDate += token.nextToken();
							
							product.setManuDate(manuDate);
						} else if(fileItem.getFieldName().equals("prodName")) {
							product.setProdName(fileItem.getString("euc-kr"));
						} else if(fileItem.getFieldName().equals("prodDetail")) {
							product.setProdDetail(fileItem.getString("euc-kr"));
						} else if(fileItem.getFieldName().equals("price")) {
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
						} else if(fileItem.getFieldName().equals("prodNo")) {  // getName()은 fileItem 고유의 이름... getFieldName()이 parameter의 이름..
							System.out.println("flag");
							product.setProdNo(Integer.parseInt(fileItem.getString("euc-kr")) );
						} else if(fileItem.getFieldName().equals("regDate")) {
							product.setRegDate(Date.valueOf((fileItem.getString("euc-kr")) ));
						}
					} /// form field check end
					else {
						
						if(fileItem.getSize() > 0) {
							
							/*
							System.out.println("getFieldName :: " + fileItem.getFieldName() );
							int index = fileItem.getFieldName().lastIndexOf("\\");
							if(index == -1)
								index = fileItem.getFieldName().lastIndexOf("/");
								
							String fileName = fileItem.getName().substring(index + 1);	
							*/
							
							// 이전 image file name을 overwrite
							String fileName = service.getProduct( product.getProdNo() ).getFileName();
							product.setFileName(fileName);
							try {
								File uploadedFile = new File(tempPath, fileName);
								fileItem.write(uploadedFile);
							} catch(IOException e) {
								e.printStackTrace();
							}
						} else {
							// 사용자가 image를 넣지 않았으면 기존 image를 그대로 가져감
							// product.setFileName("images/empty.GIF");
							product.setFileName( service.getProduct(product.getProdNo()).getFileName() );
						}
					}
				}  /// for end

				service.updateProduct(product);
				model.addAttribute(product);
			}   /// file size overflow check end
			else {
				
				int overSize = (request.getContentLength() / 1000000) ;
				System.out.println("<script>alert('file의 크기는 최대 10MB까지 지원합니다. 현재 upload하신 file의 크기는 '"
						+ overSize +"MB' 입니다.')");
				System.out.println("history.back(); </script>");
			}
		} /// multipart/form-data check end
		else {
			System.out.println("enctype이 'multipart/form-data'가 아닙니다...");
		}
		
		return "forward:/product/updateProduct.jsp";
	}


}