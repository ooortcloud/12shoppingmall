<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<!-- <link rel="stylesheet" href="/css/admin.css" type="text/css">  -->

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
 
<title>�������� ����</title>

<script src="https://code.jquery.com/jquery-2.2.4.js" integrity="sha256-iT6Q9iMJYuQiMWNd9lDyBUStIq/8PuOW33aOqmvFpqI=" crossorigin="anonymous"></script>
<script type="text/javascript">

	function checkNull() {
		let divyDate = document.querySelector('input[name="divyDate"]').value;
		if(divyDate === '') {
			alert("��� ��� ���ڴ� �ʼ� �Է� �����Դϴ�.");
			return false;
		}
		return true;
	}

	function fncUpdatePurchase() {
		if (checkNull() === true) {
			document.updatePurchase.submit();
		}
			
	}
	
	$( function() {
		
		$('button:contains("����")').on('click', function() {
			// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:fncUpdatePurchase()');
			fncUpdatePurchase();
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
		
		$('button:contains("���")').on('click', function() {
			// $(window.parent.frames['rightFrame'].document.location).attr('href', 'javascript:history.go(-1)');
			history.go(-1);
		}).on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});
	});
</script>

<script type="text/javascript" src="../javascript/calendar.js"></script>

</head>

<body bgcolor="#ffffff" text="#000000">

	<!-- ToolBar Start /////////////////////////////////////-->
	<jsp:include page="/layout/toolbar.jsp" />
   	<!-- ToolBar End /////////////////////////////////////-->



<div class="container">
		<div class="page-header">
		  <h2>���� ���� ����</h2>
		</div>
</div>

<div class='container'>
	<form name="updatePurchase" method="post"	action="/purchase/updatePurchase?tranNo=${purchase.tranNo }">
	
	<!-- name�� OGNL�� �°� �ϸ�, spring container�� �˾Ƽ� auto binding�Ѵ�. -->
	<input type="hidden" name="purchaseProd.prodNo" value="${purchase.purchaseProd.prodNo}" />
	<input type="hidden" name="buyer.userId" value="${purchase.buyer.userId }" />
	<input type="hidden" name="orderDate" value="${purchase.orderDate }" />
	
	
		<div class='form-group'>
			<label for='buyerId'>�����ھ��̵�</label>
			<input class='form-control' type='text' placeholder='${purchase.buyer.userId}' disabled>
		</div>
		
		<div class='form-group'>
			<label for='paymentOption'>���Ź��</label>
			<select 	name="paymentOption"		class="form-control" >
				<option value="1" <c:if test="${purchase.paymentOption == 1 }"> selected="selected" </c:if>>���ݱ���</option>
				<option value="2" <c:if test="${purchase.paymentOption == 2 }"> selected="selected" </c:if>>�ſ뱸��</option>
			</select>
		</div>
		
		<div class='form-group'>
			<label for='receiverName'>�������̸�</label>
			<input class='form-control' type='text' name='receiverName' placeholder='������ ������ �̸��� �Է����ּ���.' value="${purchase.receiverName }">
		</div>
		
		<div class='form-group'>
			<label for='receiverPhone'>�����ڿ���ó</label>
			<input class='form-control' type='text' name='receiverPhone' placeholder='"-"�� �����Ͽ� �Է����ּ���.' value="${purchase.receiverPhone }">
		</div>
		
		<div class='form-group'>
			<label for='divyAddr'>�������ּ�</label>
			<input class='form-control' type='text' name='divyAddr' placeholder='������ �ּҸ� �Է����ּ���.' value="${purchase.divyAddr }" >
		</div>
		
		<div class='form-group'>
			<label for='divyRequest'>���ſ�û����</label>
			<input class='form-control' type='text' name='divyRequest' placeholder='���� ��û������ �Է����ּ���.'  value="${purchase.divyRequest }">
		</div>
		
		<div class='form-group'>
			<label for='divyDate'>����������</label>
			<input 	type="text" readonly="readonly" name="divyDate" class="ct_input_g" 
							style="width: 100px; height: 19px" maxLength="20"/>
			<img 	src="../images/ct_icon_date.gif" width="15" height="15"	
						onclick="show_calendar('document.updatePurchase.divyDate', document.updatePurchase.divyDate.value)"/>
		</div>
	</form>
	
	<div class="row">
		<div class="col-sm-offset-10">
			<button type="button" class="btn btn-default">����</button>
		  	<button type="button" class="btn btn-default">���</button>
		</div>
	</div>
</div> <!-- container end -->
</body>
</html>