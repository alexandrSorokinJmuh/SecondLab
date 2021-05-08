<#import "../base.ftl" as base>

<#function string_to_int s >
    <#local a = s?replace(",", ".") >
    <#return a/>
</#function>

<@base.body "${title}">
    <form id="editForm" method="post" action="/offer/${offer.offer_id}">
        <!--    <form id="editForm" method="post">-->
        <input type="hidden" name="_method" value="put">
        <input type="hidden" value="${offer.offer_id}" name="offer_id"/>
        <div>
            <label for="name">Enter offer name: </label>
            <input type="text" value="${offer.name}" name="name" id="name"/>
        </div>
        <div>
            <label for="price">Enter price: </label>
            <input type="number" placeholder="1.0" step="0.01" min="0"
                   value="${string_to_int(offer.price?string["0.00"])}" name="price" id="price"/>
        </div>

        <table class="table">
            <tr>
                <th>Selected</th>
                <th>ID</th>
                <th>Name of paid type</th>
            </tr>

            <#list allPaidTypes?keys as paidType>
                <tr>
                    <td><label>
                            <input type="checkbox" name="paidTypes" value="${paidType}"
                                   <#if offerPaidTypes?? && offerPaidTypes?seq_contains(paidType?number)>checked</#if>>
                        </label></td>
                    <td>${paidType}</td>
                    <td>${allPaidTypes[paidType]}</td>

                </tr>
            </#list>
        </table>

        <table class="table">
            <tr>
                <th>Selected</th>
                <th>ID</th>
                <th>Name of category</th>
            </tr>

            <#list allCategories as category>
                <tr>
                    <td><label>
                            <input type="radio" name="category" value="${category.category_id }"
                                   <#if offer.category?? && offer.category.category_id == category.category_id>checked</#if>>
                        </label></td>
                    <td>${category.category_id}</td>
                    <td>${category.name}</td>

                </tr>
            </#list>
        </table>

        <div>
            <div id="characteristics">
                <#list offer.offerCharacteristics as offerCharacteristic>
                    <div class="characteristicHolder">
                        <label for="characteristic${offerCharacteristic_index}">Characteristics: </label>
                        <input type="text" class="input_characteristic" id="characteristic${offerCharacteristic_index}"
                               value="${offerCharacteristic.characteristic.name}"/>
                        <input type="hidden" name="characteristics" id="characteristic${offerCharacteristic_index}_id"
                               value="${offerCharacteristic.characteristic.characteristic_id}">
                        <label for="characteristicValue${offerCharacteristic_index}">Value:</label>
                        <input type="text" name="characteristicValues"
                               id="characteristicValue${offerCharacteristic_index}"
                               value="${offerCharacteristic.characteristicValue.value}"/>
                    </div>
                </#list>
            </div>
            <button id="addCharacteristic">Add characteristic</button>
        </div>
        <input type="submit" value="Update!"/>
    </form>

    <script>
        $("#editForm").on("submit", function (e) {

            e.preventDefault(); // avoid to execute the actual submit of the form.

            var form = $(this);
            var url = form.attr('action');

            $.ajax({
                type: "PUT",
                url: url,
                data: form.serialize(), // serializes the form's elements.
                success: function (data) {

                    console.log(data); // show response from the php script.
                    var url = "/offer/" + data["offer_id"];
                    $(location).attr('href', url);
                }
            });
        });

        $("#addCharacteristic").on("click", function (e) {
            e.preventDefault()


            let characteristicHolders = $(".characteristicHolder");
            let characteristicHolder_id = characteristicHolders.length
            let newElement = $('<div class="characteristicHolder">' +
                '<label for="characteristic' + characteristicHolder_id + '">Characteristics: </label>' +
                '<input type="text" class="input_characteristic" id="characteristic' + characteristicHolder_id + '" value=""/>' +
                '<input type="hidden" name="characteristics" id="characteristic' + characteristicHolder_id + '_id">' +
                '<label for="characteristicValue' + characteristicHolder_id + '">Value:</label>' +
                '<input type="text" name="characteristicValues" id="characteristicValue' + characteristicHolder_id + '"/>' +
                '</div>'
            )
            console.log(newElement)
            $("#characteristics").append(newElement)


            autoCompleteElement($("#characteristic" + characteristicHolder_id))


        })

        function autoCompleteElement(element) {
            $(element).autocomplete({
                source: "characteristicsNotInOffer",
                maxHeight: 400,
                maxWidth: $('#trackArtist').width,
                minLength: 1,
                select: function (event, ui) {
                    let x = "#"+this.id
                    $(x).val(ui.item.label)
                    console.log($(x))
                    console.log(ui.item)
                    console.log(ui.item.value)

                    let y = x+"_id"
                    $(y).val(ui.item.value);

                    console.log()
                    return false;
                }
            })
        }

        $(".input_characteristic").each(function () {
            autoCompleteElement(this)
        });

    </script>
</@base.body>