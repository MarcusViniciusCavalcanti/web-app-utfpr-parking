jQuery(document).ready(function ($) {
    let row = {};

    $("#users_table").dataTable({
        serverSide: true,
        processing: true,
        deferRender: true,
        responsive: true,
        "autoWidth": false,
        "language": {
            "url": "https://cdn.datatables.net/plug-ins/1.10.20/i18n/Portuguese-Brasil.json"
        },
        "createdRow": function (nRow, data) {
            $.contextMenu({
                selector: '#users_table',
                callback: function (key, options) {
                    var m = "clicked: " + key;
                    console.log('context menu row', data);
                    window.console && console.log(m, options);
                },
                items: {
                    "edit": {name: "Editar", icon: "edit",},
                    "delete": {name: "Delete", icon: "delete"},
                    "sep1": "---------",
                    "quit": {
                        name: "Quit", icon: function () {
                            return 'context-menu-icon context-menu-icon-quit';
                        }
                    }
                }
            });

            $(nRow).on('click', function (e) {
                row = nRow;
            });
        },
        "columns": [
            {"data": "id", "title": "ID"},
            {"data": "name", "title": "Nome"},
            {"data": "username", "title": "Login"},
            {"data": "plate", "title": "Placa"},
            {"data": "model", "title": "Modelo do Carro",},
            {"data": "type", "title": "Tipo"},
            {"data": "accountNonLocked", "title": "Desbloqueada"},
            {"data": "enabled", "title": "Ativa"},
        ],
        ajax: function (data, callback, settings) {
            let url = `/users/all?page=${data.start / data.length}&size=${data.length}`;
            console.log('data', data);
            console.log('data.start', data.start);
            console.log('data.length', data.length);
            console.log('data.draw', data.draw);
            console.log('settings', settings);
            console.log('calc start', data.start / data.length);

            if (settings.oPreviousSearch.sSearch !== '') {
                url = `/users/all/search/${settings.oPreviousSearch.sSearch}?page=${data.start / data.length}&size=${data.length}`;
            }
            $.ajax({
                url: url,
                dataType: 'json',
                success: function (data) {
                    console.log('ajax data', data);
                    callback({
                        data: data.content,
                        recordsTotal: data.totalElements,
                        recordsFiltered: data.totalElements
                    })
                }
            });
        }
    });


    function selectRow() {

    }
});

