<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>���� �����ȸ</title>

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
   
   
   <!-- jQuery UI toolTip ��� CSS-->
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!-- jQuery UI toolTip ��� JS-->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->

	<!--  CSS �߰� : ���ٿ� ȭ�� ������ ���� �ذ� :  �ּ�ó�� ��, �� Ȯ��-->
	<style>
        body {
            padding-top : 70px;
            padding-botton : 30px;
        }
   	</style>

<script type="text/javascript">
	function fncGetPurchaseList() {
		document.detailForm.submit();
	}
	
	function notifyArrival(event) {
		
		$( function() {
				
				$.ajax({
					
					url : "/rest/purchase/updateTranCode",
					method : "POST",
					headers : {
						"Accept" : "application/json",
						"Content-Type" : "application/json"
					},
					dataType :"JSON",
					data : JSON.stringify({
						tranNo : event.target.id,
						tranCode : "3"
						
					}),
					success : function(responseBody, httpStatus) {
						
						console.log('flag');
						$(event.target).empty();
						console.log($('#'+event.target.id).text());
						$('td#'+event.target.id).text("���� ��ۿϷ� ���� �Դϴ�.");
					}
				});
		});
	}

</script>

<script type="text/javascript">
	
	$(function() {
		
		$('tbody span').css('color', 'blue').on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
	});
</script>
    
</head>

<body bgcolor="#ffffff" text="#000000">

<jsp:include page="/layout/toolbar.jsp"/>

<div class="container">
	
	<form name="detailForm" action="/purchase/listPurchase" method="post">
	
		<input type="hidden" id="currentPage" name="currentPage" value="${resultPage.currentPage }" />
		
		<div class='form-group'>
			<label for='searchKeyword' class="col-sm-1 control-label"	>�˻�</label> 
			<div class='col-sm-5'>
				<input class='form-control' type="text" id="searchKeyword" name="searchKeyword" value="${search.searchKeyword }" />
			</div>
		</div>
	</form>
</div> <!-- container end -->	

	<div class="container">
		<div class="page-header">
			<h2>���� �����ȸ</h2>
		</div>
	</div>
	

<div class='container' id='searchList'>
	<c:set var="num" value="${resultPage.totalCount - resultPage.pageSize * (resultPage.currentPage -1 ) }" />
	<p>��ü ${requestScope.resultPage.totalCount } �Ǽ�, ���� ${resultPage.currentPage } ������</p>
	
	<table class='table table-striped'>
		<thead>
			<th>No</th>
			<th>��ǰID</th>
			<th>��ǰ��</th>
			<th>���ż���</th>
			<th>��������</th>
			<th>�����Ȳ</th>
			<th>��������</th>
		</thead>
		<tbody>
			<c:forEach var="purchase" items="${requestScope.list }">
			<tr>
				<th scope='row'>${num }</th>
				<td><a href="/product/getProduct/search?prodNo=${purchase.purchaseProd.prodNo }">${purchase.purchaseProd.prodNo }</a></td>
				<td>${purchase.purchaseProd.prodName }</td>
				<td>${purchase.numberOfPurchase }</td>
				<td>${purchase.orderDate }</td>
				<%-- tran_state_code : "1" = "���ſϷ�", "2" = "�����", "3" = "��ۿϷ�" --%>
				<td id="${purchase.tranNo }">
					<c:if test="${ purchase.tranCode == 1 }">
						���� ���ſϷ� �����Դϴ�.
					</c:if><c:if test="${ purchase.tranCode == 2 }">
						���� ����� �Դϴ�. 
					</c:if><c:if test="${ purchase.tranCode == 3 }">
						���� ��ۿϷ� ���� �Դϴ�.
					</c:if>
				</td>
				<td>
					<c:if test="${purchase.tranCode == 1 }">
						<!-- <span id="modifier1">���� ���� Ȯ�� �� ����</span>  -->
						<a href="/purchase/getPurchase?tranNo=${purchase.tranNo }">���� ���� Ȯ�� �� ����</a> 
					</c:if><c:if test="${purchase.tranCode == 2 }">
						<!-- <span id="modifier2">���� ���� �˸���</span>  -->
						<%--<a href="/purchase/updateTranCode?tranNo=${purchase.tranNo }&tranCode=3">���� ���� �˸���</a>  --%>
						<span id="${purchase.tranNo }" onclick="notifyArrival(event)">���� ���� �˸���</span>
						<p>   
					</c:if>
				</td>
			</tr>
			<c:set var="num" value="${ num-1 }" />
		</c:forEach>
		</tbody>
	</table>
</div> <!-- container end -->
	


<jsp:include page="/common/pageNavigator.jsp"></jsp:include>

</body>
</html>