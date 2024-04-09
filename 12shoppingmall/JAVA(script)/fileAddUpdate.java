
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

		
		/// Content-Type�� 'multipart/form-data'�� ���
		if(FileUpload.isMultipartContent(request)) {
			
			// file�� �ӽ÷� ����� ��� (relative path�� ����)
			//ServletContext�� getRealPath() :: �־��� path�� �����Ѵٴ� ���� �Ͽ�(?) ��ȿ�� ��θ� �˷��ش�.
			System.out.println("flag :: "+request.getServletContext().getRealPath("/images/uploadFiles"));
			String tempDirectory = request.getServletContext().getRealPath("/images/uploadFiles");  // Tomcat �� ���� ��ο� save�ؾ� eclipse���� file ���� bug ���� �� ����.
			
			
			/// ServletFileUpload�� DiskFileItemFactory�� class�� �и��Ǿ���(?)...
			DiskFileUpload fileUpload = new DiskFileUpload();  // multipart/form-data �� boundary data�� �߻�ȭ ĸ��ȭ�� ��ü
			/*
			 * setRepositoryPath :: threshold ũ�⸦ ��ģ ��� file�� �ӽ÷� �����ص� ��� ����
			 * setSizeMax :: �޾Ƶ��� �� �ִ� file�� �ִ� ũ�� ����
			 * setSizeThreshold :: disk�� direct�� ������ threshold ũ�� �Ӱ�ġ(?) ����
			 */
			fileUpload.setRepositoryPath(tempDirectory); 
			fileUpload.setSizeMax(unitKB*unitKB*10);  // 10MB
			fileUpload.setSizeThreshold(unitKB * 100);  // 100KB
			
			// input�� file ũ�Ⱑ �ִ� file ũ�⸦ ���� ���� ���
			if(request.getContentLength() < fileUpload.getSizeMax()  ) {
				
				Product tempProduct = new Product();

				List fileItemList = fileUpload.parseRequest(request);  // request body �� �� boundary�� �� parsing�ؼ� index�� ����
				int size = fileItemList.size();
				// ���������� �� boundary���� parsing�� data�� control�ϰ��� ��
				for(int i = 0; i<size; i++) {
					
					FileItem fileItem = (FileItem) fileItemList.get(i);
					
					/* 
					 *  isFormField() :: parameter�̸� true, file �����̸� false
					 */
					/// request data�� parameter ������ ���
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
						/// request data�� file ������ ���
					} else {
						
						/// binary file�� �����ϴ� ���
						if(fileItem.getSize() > 0) {
							
							/*
							 *  getName() :: client file system�� ���� filename getter
							 *  �ٵ� ���������� �츮�� ���� ������ filename �� ��ü�� �ʿ��ϴϱ� �߰������� parsing
							 */
							int index = fileItem.getName().lastIndexOf("\\");
							if(index == -1)
								index = fileItem.getName().lastIndexOf("/");
							
							// String fileName = fileItem.getName().substring(index + 1);  << ���� image�� ��� random �̸����� �ĺ��� �ο� �ʿ���
							// String extention = "." +  fileItem.getName().substring( index+1 ).split("[.]");  // dot�� ���ԽĿ��� ������ �� ���ڸ� �ǹ��ϴ� Ư�������̴�... �׷��� Ư�� ���Խ����� ���� ǥ���Ͽ� �Ϲ� ���ڷ� ����ؾ� ��.
							String extention = "." +  fileItem.getName().substring( index+1 ).split("\\.")[1];  // Ȯ����
							String fileName = new StringTokenizer( UUID.randomUUID().toString(), "-" ).nextToken() + extention;
							tempProduct.setFileName(fileName);  // view �ܿ��� static�ϰ� ��� ����ְ�, name�� �������ָ� ��
							
							try {
								File uploadedFile = new File(tempDirectory, fileName);
								fileItem.write(uploadedFile);  // save file to 'disk'
							} catch(IOException e) {
								e.printStackTrace();
							}
						} else {
							tempProduct.setFileName("images/empty.GIF");  // root ��δ� context root�ε�, ���� view���� '/image/uploadFiles'�� static path�� ���� ����
						}
					}
				}  /// for end
				
				service.addProduct(tempProduct);
				model.addAttribute("product", tempProduct);
				/// input�� file ũ�Ⱑ �ִ� file ũ�⸦ �Ѿ ���
			} else {
				
				int overSize = (request.getContentLength() / 1000000);  // �� �� ������ ������?
				System.out.println("<script>alert('file�� ũ��� �ִ� 10MB���� �����մϴ�. ���� upload�Ͻ� file�� ũ��� '"
						+ overSize +"MB' �Դϴ�.')");
				System.out.println("history.back(); </script>");
			}
			/// Content-Type�� 'multipart/form-data'�� �ƴ� ���


} else {
			System.out.println("Accept�� �� ���� encoding type�Դϴ�. ���� 'multipart/form-data'�� ����մϴ�.");
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
						} else if(fileItem.getFieldName().equals("prodNo")) {  // getName()�� fileItem ������ �̸�... getFieldName()�� parameter�� �̸�..
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
							
							// ���� image file name�� overwrite
							String fileName = service.getProduct( product.getProdNo() ).getFileName();
							product.setFileName(fileName);
							try {
								File uploadedFile = new File(tempPath, fileName);
								fileItem.write(uploadedFile);
							} catch(IOException e) {
								e.printStackTrace();
							}
						} else {
							// ����ڰ� image�� ���� �ʾ����� ���� image�� �״�� ������
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
				System.out.println("<script>alert('file�� ũ��� �ִ� 10MB���� �����մϴ�. ���� upload�Ͻ� file�� ũ��� '"
						+ overSize +"MB' �Դϴ�.')");
				System.out.println("history.back(); </script>");
			}
		} /// multipart/form-data check end
		else {
			System.out.println("enctype�� 'multipart/form-data'�� �ƴմϴ�...");
		}
		
		return "forward:/product/updateProduct.jsp";
	}


}