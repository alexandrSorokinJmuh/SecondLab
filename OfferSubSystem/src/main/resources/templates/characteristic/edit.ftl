<#import "../base.ftl" as base>

<@base.body "${title}">
    <form id="editForm" method="post" action="/characteristic/${characteristic.characteristic_id}">
<!--    <form id="editForm" method="post">-->
        <input type="hidden" name="_method" value="put">
        <input type="hidden" value="${characteristic.characteristic_id}" name="characteristic_id"/>
        <div>
            <label for="name">Enter characteristic name: </label>
            <input type="text" value="${characteristic.name}" name="name" id="name"/>
        </div>
        <div>
            <label for="description">Enter characteristic description: </label>
            <input type="text" value="${characteristic.description}" name="description" id="description"/>
        </div>
        <input type="submit" value="Update!"/>
    </form>

    <script>
        $("#editForm").on("submit", function(e) {

            e.preventDefault(); // avoid to execute the actual submit of the form.

            var form = $(this);
            var url = form.attr('action');

            $.ajax({
                type: "PUT",
                url: url,
                data: form.serialize(), // serializes the form's elements.
                success: function(data)
                {

                    console.log(data); // show response from the php script.
                    var url = "/characteristic/" + data["characteristic_id"];
                    $(location).attr('href',url);
                }
            });


        });
    </script>
</@base.body>