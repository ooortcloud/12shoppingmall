<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<!--  ///////////////////////// JSTL  ////////////////////////// -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>

	$( function() {
		
		$('div.carousel-inner').on('mouseover', function() {
			$(this).css('cursor', 'pointer');
		}).on('mouseout', function() {
			$(this).css('cursor', 'default');
		});	
	});
</script>


<div class="container">
		<div class="page-header">
		  <h1><span class="glyphicon glyphicon-heart" aria-hidden="true"></span>&nbsp;��õ ��ǰ<small>&nbsp;&nbsp;�츮 ���忡�� �����ؼ� ��õ�ϴ� ��ǰ���̿���</small></h1>
		</div>

	    <!-- Carousel
	    ================================================== -->
	    <div id="myCarousel" class="carousel slide" data-ride="carousel">
	      <!-- Indicators -->
	      <ol class="carousel-indicators">
	        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
	        <li data-target="#myCarousel" data-slide-to="1"></li>
	        <!-- <li data-target="#myCarousel" data-slide-to="2"></li>  -->
	      </ol>
	      <div class="carousel-inner" role="listbox">
	        <div class="item active" onclick=' location.href = "/product/getProduct/search?prodNo=10120" '>
	        <!-- ������ ������ ���� ���� �޾ƾ� �ϴϱ� static�ϰ� ����� ���� �ּ��̴� vs ���������� �ֿ켱������ �˰����� Ȱ���Ͽ� �ڵ����� load�ǵ��� ������ �Ѵ� -->
	          <img class="first-slide" src="/images/bigThumbnails/shampoo_big_thumbnail.webp" alt="First slide" >
	          <div class="container">
	            <div class="carousel-caption">
	              <h1>������� �������� ���� ������Ǫ</h1>
	              <!-- <p>Note: If you're viewing this page via a <code>file://</code> URL, the "next" and "previous" Glyphicon buttons on the left and right might not load/display properly due to web browser security rules.</p> -->
	              <p>Ż��, ���� ����ϰ� ��� ������ ����� ���� �Һ��ڵ��� ���� ��߿� �ǰ��� ���� �ִ� ���� ��Ǫ! �������� ���������� �������� ������ ���ļ� ������� ���� �������� ���� ��ǰ. </p>
	              <!-- image click �� page �̵��ϵ��� ���� -->
	              <!-- <p><a class="btn btn-lg btn-primary" href="#" role="button">Sign up today</a></p>  -->
	            </div>
	          </div>
	        </div>
	        <div class="item" onclick=' location.href = "/product/getProduct/search?prodNo=10051" '>
	          <img class="second-slide" src="/images/bigThumbnails/galaxy_book_4_big_ thumbnail.webp" alt="Second slide" >
	          <div class="container">
	            <div class="carousel-caption">
	              <h1>�����ú�4 ���� �Ǹ� 10�� ��� ���θ��</h1>
	              <p>�ӽŷ��װ� ������ �� AI �����ս��� �������ִ� �Ű�ó����ġ(NPU)�� �����, ���ο� ���� �ھ� ��Ʈ�� ���μ����� ž��� �����ú�4�� �Բ��ϼ���!</p>
  	              <!-- image click �� page �̵��ϵ��� ���� -->
	              <!-- <p><a class="btn btn-lg btn-primary" href="#" role="button">Sign up today</a></p>  -->
	            </div>
	          </div>
	        </div>
	        <%--
	        <div class="item">
	          <img class="third-slide" src="http://placeholder.com/1140X500" alt="Third slide">
	          <div class="container">
	            <div class="carousel-caption">
	              <h1>One more for good measure.</h1>
	              <p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
  	              <!-- image click �� page �̵��ϵ��� ���� -->
	              <!-- <p><a class="btn btn-lg btn-primary" href="#" role="button">Sign up today</a></p>  -->
	            </div>
	          </div>
	        </div>
	      </div>
	       --%>
	      <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
	        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
	        <span class="sr-only">Previous</span>
	      </a>
	      <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
	        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
	        <span class="sr-only">Next</span>
	      </a>
	    </div><!--.carousel inner end -->
	</div> <!-- carousel end -->
</div> <!-- container end -->