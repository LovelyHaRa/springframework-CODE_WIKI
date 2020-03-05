$(function () {

    const datatablesConfig = {
        "dom": 'B<"top"lf>tip',
        responsive: true,
        stateSave: true,
        order: [ 0, 'asc' ],
        "language": {
            "emptyTable": "데이터가 없습니다.",
            "lengthMenu": "_MENU_ 개씩 출력",
            "info": "_START_ - _END_ / Total: _TOTAL_",
            "infoEmpty": "-",
            "infoFiltered": "( _MAX_건의 데이터에서 필터링 되었음 )",
            "search": "검색: ",
            "zeroRecords": "일치하는 데이터가 없습니다.",
            "loadingRecords": "Loading...",
            "processing":     "Processing...",
            "paginate": {
                "next": "다음",
                "previous": "이전"
            },
            select: {
                rows: {
                    _: "%d개의 데이터 선택됨",
                    0: ""
                }
            }
        },
        select: {
            style: 'single'
        },
        buttons: [
            {
                extend: 'csvHtml5',
                text: 'Export CSV',
                className: 'btn-export-csv'
            },
            {
                text: '데이터 삭제',
                className: 'btn btn-danger btn-delete'
            }
        ]
    };

    if ($("table.user").length) {
        $(document).ready(function() {
            const table = $('table.user').DataTable(datatablesConfig);

            table.on('select', (e, dt, type, indexes) => {
                const data = table.row(indexes).data();
                const id = data[0].replace(/<(\/span|span)([^>]*)>/gi,"");
                $('.inp-user-email').val(id);
            });
            table.on('deselect', () => {
                $('.inp-user-email').val('');
            });

            $('.btn-delete').click(function () {
                const email=$('.inp-user-email').val();
                if(email==='') {
                    alert('선택된 데이터가 없습니다.');
                    return false;
                }
                const message = "정말 삭제하시겠습니까?\n삭제 데이터 email: "+ email;
                if(confirm(message)) {
                    $('#form-admin-delete').submit();
                }
            });
        });
    }
    if ($("table.board").length) {
        $(document).ready(function() {
            const table = $('table.board').DataTable(datatablesConfig);

            table.on('select', (e, dt, type, indexes) => {
                const data = table.row(indexes).data();
                const id = data[0].replace(/<(\/span|span)([^>]*)>/gi,"");
                $('.inp-board-id').val(id);
            });
            table.on('deselect', () => {
                $('.inp-board-id').val('');
            });

            $('.btn-delete').click(function () {
                const id = $('.inp-board-id').val();
                if(id==='') {
                    alert('선택된 데이터가 없습니다.');
                    return false;
                }
                const message = "정말 삭제하시겠습니까?\n삭제 데이터 id: "+ id;
                if(confirm(message)) {
                    $('#form-admin-delete').submit();
                }
            });
        });
    }
    if ($("table.barcode").length) {
        $(document).ready(function () {
            const table = $('table.barcode').DataTable(datatablesConfig);

            table.on('select', (e, dt, type, indexes) => {
                const data = table.row(indexes).data();
                const id = data[0].replace(/<(\/span|span)([^>]*)>/gi,"");
                $('.inp-barcode-id').val(id);
            });
            table.on('deselect', () => {
                $('.inp-barcode-id').val('');
            });

            $('.btn-delete').click(function () {
                const id = $('.inp-barcode-id').val();
                if (id === '') {
                    alert('선택된 데이터가 없습니다.');
                    return false;
                }
                const message = "정말 삭제하시겠습니까?\n삭제 데이터 id: " + id;
                if (confirm(message)) {
                    $('#form-admin-delete').submit();
                }
            });
        });
    }
    if ($("table.document").length) {
        $(document).ready(function () {
            const table = $('table.document').DataTable(datatablesConfig);

            table.on('select', (e, dt, type, indexes) => {
                const data = table.row(indexes).data();
                const id = data[0].replace(/<(\/span|span)([^>]*)>/gi,"");
                $('.inp-document-id').val(id);
            });
            table.on('deselect', () => {
                $('.inp-document-id').val('');
            });

            $('.btn-delete').click(function () {
                const id = $('.inp-document-id').val();
                if (id === '') {
                    alert('선택된 데이터가 없습니다.');
                    return false;
                }
                const message = "정말 삭제하시겠습니까?\n삭제 데이터 id: " + id;
                if (confirm(message)) {
                    $('#form-admin-delete').submit();
                }
            });
        });
    }
});