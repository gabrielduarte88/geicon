<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<script src="${contextPath}/res/components/jquery/dist/jquery.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/blockui/jquery.blockUI.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery-validate/dist/jquery.validate.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery-validate/dist/additional-methods.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery.scrollTo/jquery.scrollTo.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery.alphanumeric/jquery.alphanumeric.pack.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/jquery-maskedinput/dist/jquery.maskedinput.min.js" type="text/javascript"></script>
<script src="${contextPath}/res/components/iCheck/icheck.min.js" type="text/javascript"></script>

<script src="${contextPath}/res/js/tools.js" type="text/javascript"></script>

<script>
    $('document').ready(function () {
        $('form.form-default .form-control, form.form-default select').focus(function () {
            $(this).closest('.form-group').addClass('focused');
        }).blur(function () {
            $(this).closest('.form-group').removeClass('focused');
        });

        $('[data-toggle=tooltip]').tooltip({container: 'body'});;

        $('input[type=checkbox],input[type=radio]').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue'
        });
        
        $('textarea[maxlength][maxlength-label]').each(function(){
            maxlengthLimit($(this));
        });
        
        $('textarea[maxlength]').keyup(function(){  
            return maxlengthLimit($(this));
        });  
    });
    
    function maxlengthLimit(el) {
        var limit = parseInt(el.attr('maxlength'));  
            
        var label = el.attr('maxlength-label');

        var text = el.val();  

        var chars = text.length;  

        if (label) {
            $(label).html('(' + (limit - chars ) + ')');
        }

        return (chars < limit);
    }

    var loadingTimeout;
    function incrementLoadingBar() {
        $('#loader').show().css({
            'width': $('#loader').width() + ((getScreenWidth() - $('#loader').width()) / 100)
        });

        if ($('#loader').width() < getScreenWidth()) {
            loadingTimeout = setTimeout(function () {
                incrementLoadingBar();
            }, 10);
        }
    }

    function finishLoadingBar() {
        $("#loader").css({
            'width': '100%'
        });

        loadingTimeout = setTimeout(function () {
            cancelLoadingBar();
        }, 1000);
    }

    function cancelLoadingBar() {
        $("#loader").css({
            'width': 1
        }).hide();

        clearTimeout(loadingTimeout);
    }

    function reloadMenu() {
        $.ajax({
            type: 'GET',
            url: '${contextPath}/menu',
            success: function (data) {
                if (data != null) {
                    $('div.menu').replaceWith(data);

                    if (getScreenWidth() > 767) {
                        $('div.menu').addClass('open');
                        $('div.centro').removeClass('open');
                    }
                    else {
                        $('div.menu').removeClass('open');
                        $('div.centro').addClass('open');
                    }
                    
                    if ($('div.menu>div.menu-container>ul>li').length == 0) {
                        $('div.menu').hide();
                        $('button.btnMenu').hide();
                        $('div.centro').addClass('full');
                    }
                    else {
                        $('div.menu').show();
                        $('button.btnMenu').show();
                        $('div.centro').removeClass('full');
                    }
                    
                    habilitarSelecionarPesquisa();
                }
                else {
                    showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas o menu não pode ser recarregado. <br />Por favor, tente novamente em instantes.");
                    $('div.menu').hide();
                    $('button.btnMenu').hide();

                    if (getScreenWidth() > 767) {
                        $('div.centro').addClass('open');
                    }
                }
            },
            error: function () {
                showModal("<i class=\"fa fa-warning\"></i> <br /> Lamentamos, mas o menu não pode ser recarregado. <br />Por favor, tente novamente em instantes.");
                $('div.menu').hide();
                $('button.btnMenu').hide();

                if (getScreenWidth() > 767) {
                    $('div.centro').addClass('open');
                }
            }
        });
    }

    function showModal(message, extra, large) {
        var clone = $('div.modal').clone();
        
        if (large) {
            clone.addClass('large');
        }
    
        $.blockUI({
            centerY: 0,
            css: {
                backgroundColor: 'none',
                border: 'none',
                top: '40px'
            },
            overlayCSS: {
                backgroundColor: '#000',
                opacity: .4
            },
            message: clone.addClass('bounceInDown').show()
        });

        $('div.blockUI div.modal span.close').click($.unblockUI);
        $('div.blockUI div.modal div.modal-text').empty().append(message);

        if (extra) {
            $('div.blockUI div.modal div.modal-text').append(extra);
        }

        $('div.blockUI div.modal').css({'height': $('div.blockUI div.modal div.modal-text').innerHeight()});
        $('.blockOverlay').attr('title', 'Clique para voltar').click($.unblockUI);
    }
</script>