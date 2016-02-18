---
layout: none
title: SyntaxHighlighter
id: SyntaxHighlighter
root: ../
---

<head>
    <title>SyntaxHighlighter</title>
    <link media="all" rel="stylesheet" type="text/css" href="../assets/syntax/theme.css" />
</head>

<body>
   <pre class="brush: ruby">
    def show
    @widget = Widget(params[:id])
    respond_to do |format|
        format.html # show.html.erb
        format.json { render json: @widget }
    end
    end
    </pre>
    
    <script type="text/javascript" src="../assets/syntax/syntaxhighlighter.js"></script>
    <script language='javascript'>
        SyntaxHighlighter.highlight({gutter: false});
    </script>
</body>