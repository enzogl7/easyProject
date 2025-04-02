document.addEventListener("DOMContentLoaded", function () {
    const filtroProjeto = document.getElementById("filtroProjeto");

    filtroProjeto.addEventListener("change", function () {
        const projetoId = this.value;

        document.querySelectorAll(".subtarefa-card").forEach(card => {
            if (projetoId === "" || card.getAttribute("data-projeto-id") === projetoId) {
                card.style.display = "block";
            } else {
                card.style.display = "none";
            }
        });
    });
});


function criarSubtarefa() {
    var selectProjetoSubtarefa = document.getElementById('selectProjetoSubtarefa').value;
    var nomeSubtarefa = document.getElementById('nomeSubtarefa').value;
    var descricaoSubtarefa = document.getElementById('descricaoSubtarefa').value;
    var dataEntregaSubtarefa = document.getElementById('dataEntregaSubtarefa').value;
    var atribuidoASubtarefa = document.getElementById('atribuidoASubtarefa').value;

    if (!nomeSubtarefa && !descricaoSubtarefa && !dataEntregaSubtarefa) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/subtarefas/criar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            projetoSubtarefa: selectProjetoSubtarefa,
            nomeSubtarefa: nomeSubtarefa,
            descricaoSubtarefa: descricaoSubtarefa,
            dataEntregaSubtarefa: dataEntregaSubtarefa,
            atribuidoASubtarefa: atribuidoASubtarefa
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Subtarefa criada com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.href = "/subtarefas";
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao criar esta subtarefa.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function excluirSubtarefa(button) {
    var idSubtarefaExcluir = button.getAttribute('data-id');

    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir esta subtarefa?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/subtarefas/excluir',
                type: 'POST',
                data: {
                    idSubtarefa: idSubtarefaExcluir
                },
                complete: function(xhr, status) {
                    switch (xhr.status) {
                        case 200:
                            Swal.fire({
                                title: "Pronto!",
                                text: "Excluída com sucesso!",
                                icon: "success",
                                confirmButtonText: 'OK'
                            }).then(() => {
                                location.reload();
                            });
                            break;
                        default:
                            alert("Erro desconhecido: " + status);
                    }
                }
            });
        }
    })
}