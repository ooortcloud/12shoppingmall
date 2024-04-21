<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>��ٱ���</title>
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
  
  	<!--  CSS �߰� : ���ٿ� ȭ�� ������ ���� �ذ� :  �ּ�ó�� ��, �� Ȯ��-->
	<style>
        body {
            padding-top : 50px;
            padding-botton : 30px;
        }
   	</style>
   	
   	<script type="text/javascript">
   	
   		$(function(){
   			
   			// checked�� �Ǹ� �� �ݾ� ����ؼ� ����
   			$('input[type="checkbox"]').on('change', function() {
   				console.log('flag');
   			})
   		});
   	</script>
   	
</head>
<body>

	<jsp:include page="/layout/toolbar.jsp"/>
	
	<div class="container">
		<div class="page-header">
			<h2>��ٱ��� ���</h2>
		</div>
	</div>
	
	<!-- table ���� view -->
	<!-- check box�� ���� ������ ���� �ʿ� -->
	<!-- ���� ������ ��ٱ��� ������ �����Ӱ� ���� �����ϰ� ������ �� -->
	<!-- �� �ݾ��� �� ���̵��� ���� -->
	
	<!-- getProduct page���� ��ٱ��Ͽ� ��� ��� append -->
	<!-- shoppingcart ���� ���� page �ʿ� (���� page Ȱ��) -->
	<!-- addPurchase ���� �� ������ ������ ������. (batch�� ����� ����ȭ ������) -->
		
	<div class="container">
	
		<table class='table table-striped'>
		<thead>
			<th>����</th>
			<th>��ǰID</th>
			<th>��ǰ��</th>
			<th>���ż���</th>
		</thead>
		<tbody>
			<c:forEach var="item" items="${requestScope.list }">
			<tr>
				<th scope='row'>
					<div>
						<input class="checkbox" type="checkbox">
					</div>
				</th>
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
	</div>
	
	<div class="container">
		
		<!-- ���� �Ź��� ���� �� �ݾ� ���� ��� :: javascript �ʿ� -->
		<div class="row">
			<h1 class="col-md-offset-9">�� �ݾ�</h1>
		</div>
		<div class="row">
			<h2 class="col-md-offset-9 col-md-2" style="color:blue;">10000000</h2>
			<h2 class="col-md-1"> ��</h2>
		</div>
	</div>
</body>
</html>