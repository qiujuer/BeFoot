---
layout: none
title: Prismjs
id: Prismjs
root: ../
---

<head>
    <title>Prismjs</title>
    <link media="all" rel="stylesheet" type="text/css" href="../assets/prism/prism.css" />
</head>

<body>
    {% prism ruby %}
    def show
    @widget = Widget(params[:id])
    respond_to do |format|
        format.html # show.html.erb
        format.json { render json: @widget }
    end
    end
    {% endprism %}
    
    <script src="../assets/prism/prism.js" ></script>
</body>