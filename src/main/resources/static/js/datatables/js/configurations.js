jQuery(document).ready(function ($) {
    let row = {};

    const dataTable = $("#users_table").dataTable({
        serverSide: true,
        ordering: false,
        processing: true,
        deferRender: true,
        responsive: true,
        "language": {
            "url": "https://cdn.datatables.net/plug-ins/1.10.20/i18n/Portuguese-Brasil.json"
        },
        "createdRow": function (nRow, data) {
            $(nRow).mousedown('click', function (e) {
                if (e.which === 3 && $(nRow).hasClass('focused')) {
                    row = data;
                }
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
            let url = `/users/all?page=${data.start / data.length}&size=${data.length}&sort=updatedAt,DESC`;
            if (settings.oPreviousSearch.sSearch !== '') {
                url = `/users/all/search/${settings.oPreviousSearch.sSearch}?page=${data.start / data.length}&size=${data.length}&sort=updatedAt,DESC`;
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
                    });
                }
            });
        }
    });
    $.contextMenu({
        selector: '#users_table',
        callback: function (key) {
            if (key === 'edit') {
                const editUrl  = `${window.location.href}/editar/${row.id}`;
                window.location.replace(editUrl);
            } else if(key === 'delete') {
                $.confirm({
                    title: 'Cuidado!',
                    content: `Você tem certeza que deseja deletar o usuário ${row.name}?`,
                    type: 'red',
                    typeAnimated: true,
                    buttons: {
                        tryAgain: {
                            text: 'Deletar',
                            btnClass: 'btn-red',
                            action: function() {
                                console.log('action')
                            }
                        },
                        cancelar: function () {
                            console.log('close')
                        }
                    }
                })
            }
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


    const $trigger = $('#users_table');
    $trigger.contextMenu(false);

    $("#users_table tbody").on('click', 'tr', function () {
        if ($(this).hasClass('focused')) {
            $trigger.contextMenu(false);
            $(this).removeClass("focused")

        } else {
            dataTable.$('tr.focused').removeClass('focused');
            $(this).addClass('focused');
            $trigger.contextMenu(true);
        }
    });

});

