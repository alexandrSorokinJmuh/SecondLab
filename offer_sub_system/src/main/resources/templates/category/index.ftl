<#import "../base.ftl" as base>

<@base.body "${title}">
    <#list allCategory as category>
        <div>
            <a class="text-dark" href="/category/${category.category_id}">${category.name}</a>

        </div>
    </#list>
    <br/>
    <hr/>

    <a class="btn btn-info" href="/category/new">Create new category</a>

</@base.body>