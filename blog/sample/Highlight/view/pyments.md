---
layout: none
title: pygments
id: pygments
root: ../
---

<head>
    <title>pygments</title>
    <link media="all" rel="stylesheet" type="text/css" href="../assets/pygments/syntax.css" />
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