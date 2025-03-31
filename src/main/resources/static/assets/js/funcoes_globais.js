function paginacaoTabela(tabela) {
    $('#' + tabela).DataTable({
        "paging": true,
        "lengthMenu": [5, 10, 25],
        "searching": false,
        "ordering": true,
        "info": true,
        "language": {
            "lengthMenu": "_MENU_ registros por página",
            "zeroRecords": "Nenhum registro encontrado",
            "info": "Página _PAGE_ de _PAGES_",
            "infoEmpty": "Nenhum registro disponível",
            "infoFiltered": "(Filtrado de _MAX_ registros no total)",
            "paginate": {
                "previous": "Anterior",
                "next": "Próximo"
            }
        }
    });
}