var ha = ( $('#videoBox').offset().top + $('#videoBox').height() );

$(window).scroll(function(){

    if ( $(window).scrollTop() > ha +10 ) {
        $('#videoBox').css('bottom','0');
    } else if ( $(window).scrollTop() < ha ) {
        $('#videoBox').removeClass('out').addClass('in');
    } else {
        $('#videoBox').removeClass('in').addClass('out');
        $('#videoBox').css('bottom','0');
    };

});