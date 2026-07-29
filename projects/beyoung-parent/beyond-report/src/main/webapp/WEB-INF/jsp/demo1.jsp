<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>報表管理系統</title>

    <!-- CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/easyui/themes/icon.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css">

    <!-- JavaScript -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="${pageContext.request.contextPath}/jquery/jquery.cookie.js" type="text/javascript"></script>

    <style>
        html, body { margin:0; padding:0; width:100%; height:100%; overflow:hidden; }
        .ztree { padding:5px; background:#f9f9f9; }
        .header-left { float:left; padding:10px 0 0 15px; }
        .header-left img { height:40px; vertical-align:middle; }
        .header-right { float:right; padding:12px 20px 0 0; font-size:14px; line-height:30px; }
        .header-right .username { font-weight:bold; color:#2c7be5; }
        .header-right .logout-btn { margin-left:15px; }
        .tabs-panels { background:#fff; }
        /* 自適應調整 */
        @media screen and (max-width:768px) {
            .header-right { font-size:12px; padding:8px 10px 0 0; }
            .header-left img { height:30px; }
        }
    </style>

    <script type="text/javascript">
        // ======================== 全域變數 ========================
        var zNodes = [];

        // ======================== 時鐘（每30秒更新） ========================
        function ShowTime() {
            var now = new Date();
            var dayNames = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
            var timeStr = now.toLocaleString('zh-TW') + '（' + dayNames[now.getDay()] + '）';
            document.getElementById('showbox').innerHTML = '目前時間：' + timeStr;
            setTimeout(ShowTime, 30000);
        }

        // ======================== 新增分頁（保留所有權限參數） ========================
        function addTab(title, myHTML, canRead, canInsert, canSave, canDelete, canPrint, funcid) {
            if ($('#content').tabs('exists', title)) {
                $('#content').tabs('select', title);
                return;
            }
            var content;
            if (title && title.match('首頁')) {
                content = '<iframe frameborder="0" scrolling="yes" width="100%" height="100%" src="' + myHTML + '"></iframe>';
            } else {
                // 保留所有權限參數（符合原始功能）
                var params = 'funcid=' + encodeURIComponent(funcid || '') +
                             '&canRead=' + encodeURIComponent(canRead || 'N') +
                             '&canInsert=' + encodeURIComponent(canInsert || 'N') +
                             '&canSave=' + encodeURIComponent(canSave || 'N') +
                             '&canDelete=' + encodeURIComponent(canDelete || 'N') +
                             '&canPrint=' + encodeURIComponent(canPrint || 'N');
                var src = myHTML + (myHTML.indexOf('?') > -1 ? '&' : '?') + params;
                content = '<iframe frameborder="0" scrolling="yes" width="100%" height="100%" src="' + src + '"></iframe>';
            }
            $('#content').tabs('add', {
                title: title,
                content: content,
                closable: true
            });
        }

        // ======================== ZTree 設定 ========================
        var setting = {
            data: {
                key: { title: 'title' },
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: 'pId',
                    rootPId: 0
                }
            },
            callback: {
                beforeClick: function(treeId, treeNode, clickFlag) {
                    return true;
                },
                onClick: function(event, treeId, treeNode, clickFlag) {
                    // 外部連結（http開頭）
                    if (treeNode.program_name && treeNode.program_name.substring(0,4).toLowerCase() === 'http') {
                        window.open(treeNode.program_name, '_blank');
                        return;
                    }
                    // 一般功能節點（有 program_name 且非根節點）
                    if (treeNode.pId != null && treeNode.pId !== 0 && treeNode.program_name) {
                        addTab(
                            treeNode.name,
                            treeNode.program_name,
                            treeNode.canRead,
                            treeNode.canInsert,
                            treeNode.canSave,
                            treeNode.canDelete,
                            treeNode.canPrint,
                            treeNode.id
                        );
                    }
                }
            }
        };

        // ======================== 載入功能選單 ========================
        function loadMenu() {
            $('#treeDemo').html('<div style="text-align:center;padding:20px;color:#999;">載入功能選單...</div>');
            $.ajax({
                url: '${pageContext.request.contextPath}/LoginServlet',
                type: 'POST',
                data: { 'process': 'getUserFunction' },
                dataType: 'json',
                timeout: 30000,
                success: function(data) {
                    if (data && data.Success === 'Y' && data.UserFunction) {
                        zNodes = data.UserFunction;
                        var treeObj = $.fn.zTree.init($('#treeDemo'), setting, zNodes);
                        // 預設展開第一層
                        var nodes = treeObj.getNodes();
                        if (nodes && nodes.length) {
                            for (var i=0; i<nodes.length; i++) {
                                treeObj.expandNode(nodes[i], true, false, false);
                            }
                        }
                        // 自動選取第一個子節點並打開（若存在）
                        var firstChild = treeObj.getNodesByFilter(function(node) {
                            return node.pId != null && node.pId !== 0 && node.program_name;
                        });
                        if (firstChild && firstChild.length) {
                            treeObj.selectNode(firstChild[0]);
                            // 自動打開第一個功能（模擬點擊）
                            var node = firstChild[0];
                            addTab(node.name, node.program_name, node.canRead, node.canInsert,
                                   node.canSave, node.canDelete, node.canPrint, node.id);
                        }
                    } else {
                        $.messager.alert('錯誤', data?.LoginMsg || '取得功能選單失敗', 'error');
                    }
                },
                error: function(xhr, textStatus, thrownError) {
                    var msg = '系統錯誤：' + (xhr.status ? xhr.status + ' - ' : '') +
                              (xhr.responseText || thrownError || '請聯繫管理員');
                    $.messager.alert('錯誤', msg, 'error');
                }
            });
        }

        // ======================== 登出（加確認對話框） ========================
        function doLogout() {
            $.messager.confirm('確認登出', '確定要登出系統嗎？', function(r) {
                if (r) {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/LoginServlet',
                        type: 'POST',
                        data: { 'process': 'logout' },
                        dataType: 'json',
                        timeout: 10000,
                        success: function(data) {
                            // 清除客戶端 Cookie
                            $.removeCookie('COOKIE_LOGIN_USERNAME', { path: '/' });
                            if (data && data.Success === 'Y') {
                                $.messager.alert('訊息', '系統已安全登出', 'info', function() {
                                    window.parent.location.href = '${pageContext.request.contextPath}/login';
                                });
                            } else {
                                window.parent.location.href = '${pageContext.request.contextPath}/login';
                            }
                        },
                        error: function() {
                            window.parent.location.href = '${pageContext.request.contextPath}/login';
                        }
                    });
                }
            });
        }

        // ======================== 頁面初始化 ========================
        $(document).ready(function() {
            // 顯示使用者名稱（優先 Session，若無則讀取 Cookie）
            var userName = '${sessionScope.userName}';
            if (!userName || userName === '') {
                userName = $.cookie('COOKIE_LOGIN_USERNAME') || '訪客';
            }
            $('#login_id').text(userName);

            // 啟動時鐘
            ShowTime();

            // 載入功能選單
            loadMenu();
        });
    </script>
</head>

<body class="panel-noscroll">
    <!-- ======================== 主版面 ======================== -->
    <div id="cc" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;">

        <!-- ================== 上方區域 ================== -->
        <div data-options="region:'north', height:70, split:false, border:true" style="background:#fff;border-bottom:1px solid #ddd;">
            <div class="header-left">
                <img src="${pageContext.request.contextPath}/image/logo2.jpg" alt="Logo">
            </div>
            <div class="header-right">
                <span class="username" id="login_id"></span> 您好，
                <span id="showbox"></span>
                <a href="javascript:void(0)" class="easyui-linkbutton logout-btn" data-options="iconCls:'icon-logout'" onclick="doLogout();">登出</a>
            </div>
        </div>

        <!-- ================== 左側選單 ================== -->
        <div data-options="region:'west', split:true, title:'功能選項', width:240, collapse:false">
            <div id="treeDemo" class="ztree"></div>
        </div>

        <!-- ================== 中央內容區 ================== -->
        <div data-options="region:'center', border:false">
            <div id="content" class="easyui-tabs" data-options="fit:true, border:false, plain:true"></div>
        </div>
    </div>

    <!-- 額外樣式微調 -->
    <style>
        /* 確保樹節點文字清晰 */
        .ztree li a { font-size:13px; }
        .ztree li a.curSelectedNode { background:#d9e8f7; border:1px solid #99b4d1; color:#0055aa; font-weight:bold; }
        /* 標籤頁外觀 */
        .tabs li.tabs-selected .tabs-inner { color:#0055aa; font-weight:bold; background:#fff; border-bottom:2px solid #0055aa; }
        .tabs li .tabs-close { opacity:0.6; }
        .tabs li .tabs-close:hover { opacity:1; }
    </style>
</body>
</html>