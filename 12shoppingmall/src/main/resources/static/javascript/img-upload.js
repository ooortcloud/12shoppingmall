$(function() {
	
	console.log('f');
	$('input:file:last').on('change', function(event) {

		imgs = event.target.files;
		console.log(imgs);
		if (imgs.length >= 4) {
			alert('사진은 최대 3장 등록 가능합니다.');
			// event는 call by value라서 값 수정해봤자 무의미함.
			$('input:file:last').val([]);  // 빈 array로 초기화
		}
	});
});
