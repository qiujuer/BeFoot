---
layout: none
title: Rouge
id: Rouge
root: ../
---

<head>
    <title>Rouge</title>
    <link media="all" rel="stylesheet" type="text/css" href="../assets/rouge/rouge.css" />
    <style>
        pre{
            background: rgba(0, 0, 0, 0.95);
        }
    </style>
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