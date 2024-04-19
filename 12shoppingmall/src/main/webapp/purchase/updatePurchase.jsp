<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>


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
            padding-top : 70px;
            padding-botton : 30px;
        }
   	</style>

<title>���Ż���ȸ</title>

<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->

<script type='text/javascript'>
$( function() {
	
	$('button:contains("����")').on('click', function() {
		// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:fncUpdatePurchase()');
		window.location.href = "/purchase/updatePurchase?tranNo=${purchase.tranNo}";
	}).on('mouseover', function() {
		$(this).css('cursor', 'pointer');
	}).on('mouseout', function() {
		$(this).css('cursor', 'default');
	});
	
	$('button:contains("Ȯ��")').on('click', function() {
		// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:history.go(-1)');
		window.location.href = "/purchase/listPurchase";
	}).on('mouseover', function() {
		$(this).css('cursor', 'pointer');
	}).on('mouseout', function() {
		$(this).css('cursor', 'default');
	});
});
</script>

</head>

<body bgcolor="#ffffff" text="#000000">

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->



<div class="container">
		<div class="page-header">
		  <h2>���� �� ��ȸ</h2>
		</div>
</div>


<div class='container'>
<table class='table'>
	<tbody>
	<tr>
		<th scope="row">��ǰ��ȣ</th>
		<!-- object scope ���� :: pageScope < requestScope < sessionScope < applicationScope -->
		<td>${requestScope.purchase.purchaseProd.prodNo }</td>
	</tr>
	<tr>
		<th scope='row'>�����ھ��̵�</th>
		<td>${purchase.buyer.userId }</td>
	</tr>
	<tr>
		<th scope='row'>���Ź��</th>
		<td>
			<c:if test="${purchase.paymentOption == '1' }">
			���ݱ��� 
			</c:if><c:if test="${purchase.paymentOption == '2' }">
			�ſ뱸��
			</c:if>
		</td>
	</tr>
	<tr>
		<th scope='row'>�������̸�</th>
		<td>${purchase.receiverName }</td>
	</tr>
	<tr>
		<th scope='row'>�����ڿ���ó</th>
		<td>${purchase.receiverPhone }</td>
	</tr>
	<tr>
		<th scope='row'>�������ּ�</th>
		<td>${purchase.divyAddr }</td>
	</tr>
	<tr>
		<th scope='row'>���ſ�û����</th>
		<td>${purchase.divyRequest }</td>
	</tr>
	<tr>
		<th scope='row'>����������</th>
		<td>${purchase.divyDate }</td>
	</tr>
	<tr>
		<th scope='row'>�ֹ���</th>
		<td>${purchase.orderDate }</td>
	</tr>
	<tr>
		<th scope='row'>���� ����</th>
		<td>${purchase.numberOfPurchase }</td>
	</tr>
	</tbody>
</table>

	<div class="row">
		<div class="col-sm-offset-10">
			<button type="button" class="btn btn-default">�ٽ� ����</button>
		  	<button type="button" class="btn btn-default">Ȯ��</button>
		</div>
	</div>
</div> <!-- container end -->
</body>
</html>