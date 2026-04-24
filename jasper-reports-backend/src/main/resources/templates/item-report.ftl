<html>
<head>
    <meta charset="UTF-8">
    <style>
        body { 
            /* 建議同時加上英文與中文名稱以確保相容性 */
            font-family: "Microsoft JhengHei", "微軟正黑體", sans-serif;        }
        h2 { text-align: center; color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th { background-color: #f2f2f2; font-weight: bold; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
    </style>
</head>
<body>
    <h2>庫存品項明細報表 (FreeMarker)</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>名稱</th>
                <th>庫存量</th>
            </tr>
        </thead>
        <tbody>
            <#list data as item>
            <tr>
                <td>${item.id}</td>
                <td>${item.itemName}</td>
                <td>${item.stockQuantity}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</body>
</html>