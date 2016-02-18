---
layout: none
title: Highlight.js
id: Highlight.js
root: ../
---

<head>
    <title>Highlight.js</title>
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.1.0/styles/default.min.css">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.1.0/highlight.min.js"></script>
    <script type="text/javascript">
        hljs.initHighlightingOnLoad();
    </script>
</head>

<body>
    {% highlight ruby %}
    def show
    @widget = Widget(params[:id])
    respond_to do |format|
        format.html # show.html.erb
        format.json { render json: @widget }
    end
    end
    {% endhighlight %}
</body>