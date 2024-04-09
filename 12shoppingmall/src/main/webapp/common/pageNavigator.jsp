<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ page pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 
<div class="container text-center">
		 
		 <nav id="page-navigator">
		  <!-- ũ������ :  pagination-lg pagination-sm-->
			  <!-- <ul class="pagination" >  -->
			    <ul class="pager" id="pager" >
			    <!--  <<== ���� nav -->
			    <!-- ���� page�� page ����(?)���� ���� ��� ���� page ���� �̵� link ���ʿ� -->
			  	<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }">
			 		<li class="disabled">
				</c:if>
				<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
					<li>
						<a href="javascript:fncGet<c:if test="${requestScope.title == 'user' }">User</c:if><c:if test="${requestScope.title == 'product' }">Product</c:if><c:if test="${requestScope.title == 'purchase' }">Purchase</c:if>List('${ resultPage.beginUnitPage-1}')" aria-label="Previous">
							<!--  <a href="javascript:fncGetUserList('${ resultPage.currentPage-1}')" aria-label="Previous">  -->
					        <span aria-hidden="true">&laquo;</span>
				        </a>
				</c:if>
				    </li>
			    
			    <!--  �߾�  -->
				<c:forEach var="i"  begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
					
					<c:if test="${ resultPage.currentPage == i }">
						<!--  ���� page ����ų��� : active -->
					    <li class="active">
					    	<!-- <a href="javascript:fncGetUserList('${ i }');">${ i }<span class="sr-only">(current)</span></a>  -->
					    	<a href="javascript:fncGet<c:if test="${requestScope.title == 'user' }">User</c:if><c:if test="${requestScope.title == 'product' }">Product</c:if><c:if test="${requestScope.title == 'purchase' }">Purchase</c:if>List('${ i }');">${ i }</a>
					    </li>
					</c:if>	
					
					<c:if test="${ resultPage.currentPage != i}">	
						<li>
							<!-- <a href="javascript:fncGetUserList('${ i }');">${ i }</a>  -->
							<a href="javascript:fncGet<c:if test="${requestScope.title == 'user' }">User</c:if><c:if test="${requestScope.title == 'product' }">Product</c:if><c:if test="${requestScope.title == 'purchase' }">Purchase</c:if>List('${ i }');">${ i }</a>
						</li>
					</c:if>
				</c:forEach>
			    
			     <!--  ���� nav==>> -->
			     <!-- �ִ� page���� ���� ���� ������ page�� �� ũ�� �� ���� page �������� �̵� ���ʿ� -->
			     <c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }">
			  		<li class="disabled">
				</c:if>
				<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
					<li>
				     	<!--  <a href="javascript:fncGetUserList('${resultPage.endUnitPage+1}')" aria-label="Next">  -->
				     	<a href="javascript:fncGet<c:if test="${requestScope.title == 'user' }">User</c:if><c:if test="${requestScope.title == 'product' }">Product</c:if><c:if test="${requestScope.title == 'purchase' }">Purchase</c:if>List('${resultPage.endUnitPage+1}')">
				        <span aria-hidden="true">&raquo;</span>
				      </a>
				</c:if>
				    </li>
		 	 </ul>  <!-- pager end -->
		</nav>
</div>
 

<!-- 
<div class="container">
		<nav>
		  <ul class="pager">
		    <li><a href="#">Previous</a></li>
		    <li><a href="#">Next</a></li>
		  </ul>
		</nav>
</div>
 -->

<!-- 
<div class="container">
		<nav>
		  <ul class="pager">
		    <li class="previous disabled"><a href="#"><span aria-hidden="true">&larr;</span> Older</a></li>  -->
		    <!-- <li class="previous"><a href="#"><span aria-hidden="true">&larr;</span> Older</a></li>  -->
		 <!--    <li class="next"><a href="#">Newer <span aria-hidden="true">&rarr;</span></a></li>
		  </ul>
		</nav>
</div>
 -->