/*
function reSizeImg(border, image) {
	
	const div = border; // 이미지를 감싸는 div
	const img = image; // 이미지
	const divAspect = 200 / 243; // height / width [px]
	const imgAspect = img.height / img.width;
	
	if (imgAspect <= divAspect) {
	    // 이미지가 div보다 납작한 경우 세로를 div에 맞추고 가로는 잘라낸다
	    let imgWidthActual = div.offsetHeight / imgAspect;
	    let imgWidthToBe = div.offsetHeight / divAspect;
	    let marginLeft = -Math.round((imgWidthActual - imgWidthToBe) / 2);
	    img.style.cssText = 'width: auto; height: 100%; margin-left: '
	                      + marginLeft + 'px;'
	} else {
	    // 이미지가 div보다 길쭉한 경우 가로를 div에 맞추고 세로를 잘라낸다
	    img.style.cssText = 'width: 100%; height: auto; margin-left: 0;';
	}
}
*/


function reSizeImg(img) {
	
	let borderWidth = 243;
	let imgWidth = img.css('width');
	
	console.log(imgWidth);
}


