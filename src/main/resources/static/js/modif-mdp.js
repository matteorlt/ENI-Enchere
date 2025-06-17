var Mdp = document.getElementById("inputMdp")
var ConfirmMdp = document.getElementById("inputConfirmMdp")
var phraseMdp = document.getElementById("textMdp")
var phraseConfirmMdp = document.getElementById("textConfirmMdp")

document.getElementById("inputMdp").addEventListener("input", function () {
    let forceMdpFaible = document.getElementById("faible")
    let forceMdpMoyen = document.getElementById("moyen")
    let forceMdpFort = document.getElementById("fort")
    let regex = /^(?=.*[0-9])(?=.*[A-Za-z])(?=.*\W)(?!.* ).{8,}$/;

    forceMdpFaible.style.visibility = "visible";

    if (Mdp.value.match(regex)) {
        phraseMdp.classList.remove("red");
    }
    else {
        Mdp.style.visibility = "visible";
        phraseMdp.classList.add("red");
    }

    if (Mdp.value.match(/^(?=.*[\d\W])(?!.* ).{8,}$/)) {
        forceMdpMoyen.style.visibility = "visible";
        if (Mdp.value.match(/^(?=.*[0-9])(?=.*[A-Za-z])(?=.*\W)(?!.* ).{12,}$/)) {
            forceMdpFort.style.visibility = "visible";
        }
    }
})

document.getElementById("inputConfirmMdp").addEventListener("input", function () {

    if (ConfirmMdp.value == Mdp.value) {
        phraseConfirmMdp.style.visibility = "hidden";
    }
    else {
        phraseConfirmMdp.style.visibility = "visible";
    }
})

document.getElementById("form").addEventListener("input", function () {
    let buttonEnable = document.getElementById("btnSubmit");
    let champValide = document.querySelectorAll(":valid");
    if (champValide.length == 8) {
        buttonEnable.classList.remove("disabled");
    }
    if (champValide.length < 8) {
        buttonEnable.classList.add("disabled");
    }
})

