<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
    <title>報表管理系統</title>
    <meta name="goolebot" content="noarchive">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="css/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="css/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="css/demo.css">
    <LINK rel=stylesheet type=text/css href="css/Frame.css">
    
    <script src="jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="jquery/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
    <script src="jquery/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="jquery/jquery.form.js" type="text/javascript"></script>
    <script type="text/javascript" src="jquery/jquery.blockUI.js"></script>
    <script src="jquery/jquery.cookie.js" type="text/javascript"></script>

    <script> 
        $(document).ready(function() { 
            $('#myForm').submit(function() { 
                var options = { 
                        url: 'LoginServlet',
                        type: 'post',
                        dataType: 'json',
                        beforeSubmit:  beforeSubmit,
                        success: successSubmit,
                        error: function(xhr, ajaxOptions, thrownError){
                            $.unblockUI();
                            $(".blockUI").fadeOut("slow"); 
                            $.messager.alert('錯誤視窗',xhr.responseText, "error");
                        }
                };                  
                $(this).ajaxSubmit(options); 
                return false; 
            }); 
         });
    
         function beforeSubmit(formData, jqForm, options) { 
            if (jqForm.form('validate')){
                $.blockUI({message: "<div style='font-size:18px;font-weight:bold'>系統登入中，請稍候....</div>", css: { 
                    border: 'none', 
                    padding: '15px', 
                    backgroundColor: '#000', 
                    '-webkit-border-radius': '10px', 
                    '-moz-border-radius': '10px', 
                    opacity: .5, 
                    color: '#fff' 
                } }); 
            }
            return jqForm.form('validate'); 
         } 
    
        function successSubmit(responseText, statusText, xhr, $form)  { 
            $.unblockUI();
            $(".blockUI").fadeOut("slow"); 
            if (responseText.Success == 'Y'){
                $(location).attr('href', "demo1"); 
            }else{
                $.messager.alert('警告視窗',responseText.LoginMsg, "warning", function(){});    
            }
        } 
    </script> 
</HEAD>
<BODY leftMargin=0 scroll=no topMargin=0>
    <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" height="100%">
        <TBODY>
            <TR>
                <TD align=middle>
                    <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
                        <TBODY>
                            <TR>
                                <TD
                                    style="PADDING-BOTTOM: 6px; PADDING-LEFT: 6px; PADDING-RIGHT: 6px; PADDING-TOP: 6px"
                                    height=66 vAlign=bottom align=right><IMG src="image/logo2.jpg"></TD>
                                <TD vAlign=bottom width="30%"><IMG src="image/logo_new.png"
                                    width=351 height=63></TD>
                                <TD>&nbsp;</TD>
                            </TR>
                            <TR>
                                <TD bgColor=#9699e0 width="50%" align=right><IMG src="image/loginbgleft.jpg" width=381 height=190></TD>
                                <TD
                                    style="PADDING-BOTTOM: 6px; PADDING-LEFT: 6px; PADDING-RIGHT: 6px; PADDING-TOP: 6px"
                                    bgColor=#acaedb align=middle>
                                    <FORM id=myForm>
                                        <input type="hidden" name="process" value="login" />
                                        <input type="hidden" name="auto" value="N" />
                                        <TABLE border=0 cellSpacing=0 cellPadding=0 width=300>
                                            <TBODY>
                                                <TR>
                                                    <TD width=1><IMG src="image/lcornerleft.jpg" width=13
                                                        height=142></TD>
                                                    <TD bgColor=#bdbee2>
                                                        <TABLE border=0 cellSpacing=0 cellPadding=3 width="100%">
                                                            <TBODY>
                                                                <TR>
                                                                    <TD colSpan=2 align=middle><SPAN id=lblMessage></SPAN></TD>
                                                                </TR>
                                                                <TR>
                                                                    <TD style="WIDTH: 73px"><SPAN id=lblLogonName>帳號:</SPAN></TD>
                                                                    <TD><INPUT
                                                                        style="BACKGROUND-COLOR: white; WIDTH: 218px"
                                                                        id=userid class="Underline_Field "
                                                                        name=userid></TD>
                                                                </TR>
                                                                <TR>
                                                                    <TD style="WIDTH: 73px"><SPAN id=lblPassword>密碼:</SPAN></TD>
                                                                    <TD><INPUT
                                                                        style="BACKGROUND-COLOR: white; WIDTH: 218px"
                                                                        id=password class="Underline_Field " type=password
                                                                        name=password></TD>
                                                                </TR>
                                                                <TR>
                                                                    <TD height=25 vAlign=center colSpan=2 align=middle><SPAN
                                                                        style="COLOR: indigo; FONT-SIZE: 12px"
                                                                        id=lblLoginMessage>請使用與網域相同之帳號與密碼</SPAN></TD>
                                                                </TR>
                                                                <TR>
                                                                    <TD>&nbsp;</TD>
                                                                    <TD><INPUT id=btnLogon value="登入" type=submit
                                                                        name=btnLogon>&nbsp;&nbsp; <INPUT id=btnClear
                                                                        value="清除" type=reset name=btnClear></TD>
                                                                </TR>
                                                            </TBODY>
                                                        </TABLE>
                                                    </TD>
                                                    <TD width=1><IMG src="image/lcornerright.jpg" width=13
                                                        height=142></TD>
                                                </TR>
                                            </TBODY>
                                        </TABLE>
                                       
                                        <input type="hidden" name="auto" value="N" />
                                    </FORM>
                                </TD>
                                <TD style="BORDER-LEFT: #ffffff 1px solid" bgColor=#9699e0></TD>
                            </TR>
                        </TBODY>
                    </TABLE>
                    <P>比漾廣場版權所有 Copyrightc 2017 Beyond Plaza Corporation
                        Limited. All Rights Reserved.</P>
                    <P>&nbsp;</P>
                </TD>
            </TR>
        </TBODY>
    </TABLE>
    <script type="text/javascript">
        $("#userid").val($.cookie("COOKIE_LOGIN_USERNAME"));
        $("#password").val($.cookie("COOKIE_LOGIN_PASSWD"));
    </script>  
</BODY>
</HTML>