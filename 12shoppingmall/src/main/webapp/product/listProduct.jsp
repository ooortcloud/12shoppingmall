<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%--  JSP ���������� JSTL Core ���̺귯���� ����ϱ� ���� �±� ���̺귯�� ���� --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>��ǰ ����</title>

<!-- ���� : http://getbootstrap.com/css/   ���� -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!--  ///////////////////////// Bootstrap, jQuery CDN ////////////////////////// -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" >
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
	
	<!-- Bootstrap Dropdown Hover CSS -->
   <link href="/css/animate.min.css" rel="stylesheet">
   <link href="/css/bootstrap-dropdownhover.min.css" rel="stylesheet">
   
    <!-- Bootstrap Dropdown Hover JS -->
   <script src="/javascript/bootstrap-dropdownhover.min.js"></script>

	<!--  ///////////////////////// CSS ////////////////////////// -->
	<style>
		body {
            padding-top : 50px;
            padding-bottom : 30px;
        }
    </style>


<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->
 
<script type="text/javascript">
	function fncGetProductList(currentPage) {
		console.log("currentPage = " + currentPage);
		document.getElementById("currentPage").value = currentPage;
 	  	document.detailForm.submit();		
	}
	
	function compare() {
		// input �±װ��� ������ �� �� �������ִ� �Լ��� ����Ͽ� ��������.
		// const min = Number(document.getElementById("priceMin").value);
		// const max = Number(document.getElementById("priceMax").value);
		const min = Number(document.querySelector('input[id="priceMin"]').value);
		const max = Number(document.querySelector('input[id="priceMax"]').value);
		
		console.log("min = " + min);
		console.log("max = " + max);
		
		if( (min < 0) || (max > 2147483647) ) {
			alert("�Է� ������ �ʰ��Ͽ����ϴ�.");
		}
		
		if(min != 0 && max != 0) {
			if(min >= max) {
				alert("�ּҰ��� �ִ밪 �̸��� �ǵ��� �ۼ����ּ���.");
				return ;
			}
		}
		fncGetProductList(document.getElementById("currentPage").value );	
	}

	
	$(function() {
		
		$('button:contains("�˻�")').on('click', function() {
			document.getElementById('detailForm').submit();
		});
	});
</script>

<!-- jQuery core library �ܿ��� �پ��� library �߰��ؾ� �� -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.12.4.js"></script>
<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script> 
<script type="text/javascript">



	$( function() {  
		
		$('a').css('text-decoration', 'none');   
		
		$('.ct_btn01:contains("�˻�")').on('click', function() {
			compare();
		}).on('mouseover', function() {
			$('.ct_btn01:contains("�˻�")').css('cursor', 'pointer');
		}).on('mouseout', function() {
			$('.ct_btn01:contains("�˻�")').css('cursor', 'default');
		});
		
		/* ���� �ʿ�� ������... ���� hyperlink ����̶�, ajax �����丵 ����� �� ŭ
		$('li[role="presentation"]:active').on('click', function() {
			console.log('flag');
			$('li[role="presentation"]:active').attr('active', '');
		});
		*/
	});
	
	
	function setTable(responseBody, httpStatus) {
		
		//  list ������ŭ �ݺ��ؼ� append�ϸ� ��...
		let num = responseBody.resultPage.totalCount;
		// JSTL�� ����ϸ� ������� event�� ���� �������� ȭ�� ��ȯ�� �Ұ���...
		searchList.append("<p>��ü "+num+" �Ǽ�, ���� "+1+" ������</p>");

		/// table head
		let temp = "";  
		temp = "<table class='table table-striped'><thead>"
						+"<th>No</th>"
						+'<th>��ǰ��</th>'
						+'<th>����</th>'
						+'<th>�����</th>'
						+'<th>�������</th>'
					+"</thead>";
		
		/// table row
		temp += "<tbody>"
		for(const item of responseBody.list) { 
			temp += "<tr>";
			temp += "<th scope='row'>"+num+"</th>";
			temp += "<td><a href='/product/getProduct?prodNo="+item.prodNo+"&menu="+menu+"'>"+item.prodName+"</a></td>";
			temp += "<td>"+item.price+"</td>";
			temp += "<td>"+new Date(item.regDate).toLocaleDateString()+"</td>";  // Date ���Ŀ� �°� ��ȯ �ʿ�
			temp += "<td>";
			let presentState = item.proTranCode;
			if(presentState == null || presentState == '0') {
				temp += "�Ǹ� ��";
			} else {
				if(menu == "manage") {
					// switch �� �������� type���� ����Ͽ� ���� ('===')
					switch(presentState) {
						case '1':
							temp += "<a id='doDelivery' href='/purchase/updateTranCodeByProd?prodNo="+item.prodNo+"&tranCode=2'>client���� ����ϱ�</a>";
							break;
						case '2':
							temp += "��� ��";
							break;
						case '3':
							temp += "��� �Ϸ�";
							break;
						default:
							console.log("error");
							break;
					}
				} else {
					temp += "��� ����";
				}
			}
			temp += '</td></tr>'; 
			
			num--;
		}  /// for end
		temp += "</tbody></table>";
		searchList.append(temp);
		
		/// page navigator
		temp="";
		const pageNavigator = $('#pager');
		pageNavigator.empty();  // navigator �ʱ�ȭ
		if(responseBody.resultPage.currentPage <= responseBody.resultPage.pageUnit) {
			temp += "<li class='disabled'>";
		} else {
			temp += "<li><a href='javascript:fncGetProductList("+(responseBody.resultPage.beginUnitPage - 1)+")'><span aria-hidden='true'>&laquo;</span></a></li>";
		}

		for(let i = responseBody.resultPage.beginUnitPage; i < responseBody.resultPage.endUnitPage + 1; i++ ) {
			if(i==1) // �˻��ϸ� ������ ù page�� ȸ��
				temp += "<li class='active'>";
			else
				temp += "<li>";
			temp += "<a href='javascript:fncGetProductList("+i+")'>"+i+"</a>";  
			temp += "</li>";
		}
		
		console.log("maxPage :: " + responseBody.resultPage.maxPage);
		if(responseBody.resultPage.endUnitPage >= responseBody.resultPage.maxPage) {
			temp += "<li class='disabled'>";
		} else {
			temp += "<li><a href='javascript:fncGetProductList("+(responseBody.resultPage.endUnitPage + 1)+")'><span aria-hidden='true'>&raquo;</span></a></li>";
		}
		pageNavigator.append(temp);
	}
	
	$( function() { 
		
		const inputKeyword = $('#searchKeyword');
		
		inputKeyword.on('click', function() {  // ù �Է� ���� ���ſ�
			
			console.log('searchKeyword �Է� ���...');
		});
		
		inputKeyword.on('keyup', function(event) {  // ����ڰ� ���ڸ� �Է����� �� :: event�� ���� �پ��� ���� ���Թޱ� ����

			/// �������� ���� �Է��� �ƴ� ��� request �� ����.
			// keyCode :: ASCII code value
			console.log("flag = " + event.keyCode);
			if( event.keyCode <= 31 ) {
				return ;
			}
		
			$.ajax( {
				 
				url : "/rest/product/json/listProduct/"+$('input[name="menu"]').val()+"/autocomplete",
				method : "POST",
				dataType : "JSON",
				headers : {
					"Accept" : "application/json",
					"Content-Type" : "application/json"
				},
				/*
					JSON ��ü :: query string���� �÷� ����  << URL�� ������  @ModelAttribute
					JSON string :: body�� �״�� ��ܼ� ����  @RequestBody
				*/
				data : JSON.stringify( {
					searchKeyword : inputKeyword.val(),
					priceMin : $('#priceMin').val(),
					priceMax : $('#priceMax').val(),
					currentPage : "1"
				}),   
				success : function(responseBody, httpStatus) {
					
					console.log("server data ����")
					const arr = new Array();
					// JSON array object�κ��� �ʿ��� data�� �����ؼ� array ����
					for (const item of responseBody.list) { 
						arr.push(item.prodName); 
					}
					if(responseBody != null) { 
						
						console.log(arr); 

						// ���� page ù load �� ù ���� �Է� �� Ȱ��ȭ�� �� �Ǵ� ���� ����...
						inputKeyword.autocomplete({ 
							
							source: arr,  // �˻� ���
							/*
								callback
								event :: ���� �۵��� event listener (����)
								ui :: ���� ��� ���� jQuery object
							*/
							select: function(event, ui) {  // callback :: ����ڰ� ��õ �˻�� Ŭ������ ��
								
								inputKeyword.autocomplete('close');  // select �� ��õ �˻� ����Ʈ �ڵ� �ݱ�
	
								console.log("list �籸���� �ʿ��� data load...")
								$.ajax( { 
									
									url : "/rest/product/json/listProduct/search",
									method : "POST",
									dataType : "json",
									headers: { 
										"Accept" : "application/json",
										"Content-Type" : "application/json"
									},
									data: JSON.stringify({
										currentPage : "1",
										// searchKeyword : inputKeyword.val()
										searchKeyword : ui.item.value
									}),
									// ���� UI�� ��������...
									success: function(responseBody, httpStatus) {

										console.log("list �籸��...")
										const searchList = $('#searchList');
										searchList.empty();
										
										const menu = $('#menu').val();
										if(menu  == 'manage' ) {
											setTable(responseBody, httpStstus);	
										}
										/// '��ǰ �˻�'������ ���� scroll�� �籸��
										else if ($('#menu').val() == 'search') {
											
										} else {
											// nothing
										}
									}
								});  // list �籸�� end
							}
						});  /// autocomplete callback end
					} else { // if( responseBody === null )
						$('#searchKeyword').autocomplete( 'destroy');
					}
				}  /// success end
			} );  /// ajax end
		});  /// keyup callback end
	});
</script>
</head>

<body>


	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->

<div class="container">
	<div class="page-header">
		<h2>
			<%-- parameter�� ���� data���� JSTL�� 'param' ���� ��ü���� ������ --%>
			<%-- EL ���ο����� '�� ����ؼ� ���ε� ���ڿ��� ����� --%>
			<c:if test="${menu == 'manage' }">
				��ǰ ����
			</c:if><c:if test="${menu == 'search' }">
				��ǰ �����ȸ
			</c:if>
		</h2>
	</div>


<form class="form-horizontal" id="detailForm"  name="detailForm" action="/product/listProduct/${menu }" method="post">

<input type="hidden" id="currentPage" name="currentPage" value="${resultPage.currentPage }" />
<input type="hidden" id="menu" name="menu" value="${menu }" />
<input type="hidden" id='priceDESC' name="priceDESC" value="${search.priceDESC}" />

	<div class="form-group">
		<label for="searchKeyword" class="col-sm-1 control-label" >��ǰ��</label>
		<div class="col-sm-5">
			<input type="text" class="form-control" id="searchKeyword" name="searchKeyword" value="${search.searchKeyword }" placeholder = "��ǰ��" />
		</div>
	</div>

	<div class="form-group">
		<label for="priceMin" class="col-sm-1 control-label">��ǰ����</label>
		<div class="col-sm-2">
			<input type="number" class="form-control"  id="priceMin" name="priceMin" value="${search.priceMin }" placeholder="�ּ� ����" />
		</div>
		<div class="col-sm-1 text-center">
			&nbsp;~&nbsp;
		</div>
		<div class="col-sm-2">
			<input type="number" class="form-control"  id="priceMax" name="priceMax" value="${search.priceMax }" placeholder = "�ִ� ����" />
		</div>
	</div>


		 
</form>

	<div class="row">
		<div class="col-sm-11">
		<ui class="nav nav-pills" role="tablist">
			 <!-- ���� ���� ����� Ŭ���� ��� -->
			<c:if test="${ !empty search.priceDESC}">  
				<!-- �Է��� ���� box�� ���� query string ���� ���� -->
				<c:if test="${search.priceDESC == 0 }">
					<li role="presentation" class="active"><a href="/product/listProduct/${menu}?priceDESC=0<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
					<li role="presentation"><a href="/product/listProduct/${menu}?priceDESC=1<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
				</c:if><c:if test="${search.priceDESC == 1 }">
					<li role="presentation"><a href="/product/listProduct/${menu}?priceDESC=0<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
					<li role="presentation" class="active"><a href="/product/listProduct/${menu}?priceDESC=1<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
				</c:if>
			</c:if><c:if test="${empty search.priceDESC}"> <!-- ����Ʈ --> 
				<li role="presentation"><a href="/product/listProduct/${menu}?priceDESC=0<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
				<li role="presentation"><a href="/product/listProduct/${menu}?priceDESC=1<c:if test='${ !empty search.searchKeyword }'>&searchKeyword=${search.searchKeyword }</c:if><c:if test='${ !empty search.priceMin }'>&priceMin=${search.priceMin }</c:if><c:if test='${ !empty search.priceMax }'>&priceMax=${search.priceMax }</c:if>">�������ݼ�</a></li>
			</c:if> 
		</ui>
		</div>
		<div class="col-sm-1">
			<button type="button" class="btn btn-default">�˻�</button>
		</div>
	</div>
</div> <!-- container end -->


	<c:if test="${menu=='search' }">
		<div class="container">
			<div class="page-header">
				<h2>��ǰ ����Ʈ</h2>
			</div>
		</div>
		<jsp:include page="/product/mainContents.jsp"></jsp:include>
	</c:if>
	<c:if test="${menu=='manage' }">
	
	<%-- JSTL���� ���ú��� ���� ���� --%>
	<c:set var="num" value="${resultPage.totalCount - resultPage.pageSize * (resultPage.currentPage -1 ) }" />
	<div class="container" id="searchList">
		<p>��ü ${requestScope.resultPage.totalCount } �Ǽ�, ���� ${resultPage.currentPage } ������</p>

		<table class="table table-striped">
			<thead>
				<th>No</th>
				<th>��ǰ��</th>
				<th>����</th>
				<th>�����</th>
				<th>�������</th>
			</thead>
			<tbody>
				<%-- JSTL���� index ���� Collection�� ������ �ݺ��� ����ϱ� --%>
				<c:forEach var="product" items="${requestScope.list}">
					<tr>
						<th scope="row">${num }</th>
						<td><a href="/product/getProduct/${menu}?prodNo=${product.prodNo }">${product.prodName }</a></td>
						<td>${product.price }</td>
						<td>${product.regDate }</td>
						<td>
							<%-- JSTL���� ���� ������ ���� ��� ���� --%>
							<c:set var="presentState" value="${product.proTranCode }" />
							<c:if test="${ (empty presentState) || presentState == 0 }">
								�Ǹ� ��
							</c:if><c:if test="${ presentState != 0}">
								<c:if test="${menu == 'manage' }">
									<c:choose>
										<c:when test="${presentState == 1 }">
											<!-- <span id="doDelivary">client���� ����ϱ�</span>  --> 
											<a id="doDelivery" href='/purchase/updateTranCodeByProd?prodNo=${product.prodNo }&tranCode=2'><strong> client���� ����ϱ�</strong></a> 
										</c:when><c:when test="${presentState == 2 }">
											��� ��	
										</c:when><c:when test="${presentState == 3 }">
											��� �Ϸ�
										</c:when>
									</c:choose>
								</c:if><c:if test="${menu == 'search' && !( (empty presentState) || presentState == 0 ) }">
									��� ����
								</c:if>
							</c:if>
							</td>
						</tr>
						<c:set var="num" value="${num-1 }" />
				</c:forEach>
			</tbody>
		</table>
	</div> <!-- container end -->
	</c:if>


	<!-- search������ ���� scroll -->
	<c:if test="${menu == 'manage' }">
		<%-- �ϴ� ������ .jsp �����include --%>
		<jsp:include page="/common/pageNavigator.jsp"></jsp:include>
	</c:if>

</body>
</html>
