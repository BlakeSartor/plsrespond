let id = id => document.getElementById(id);

let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
ws.onmessage = msg => updateChat(msg);
ws.onclose = () => alert("ohhh noooooooooo");

id("message").addEventListener("click", () => literallyJustToScroll());

id("send").addEventListener("click", () => sendAndClear(id("message").value));
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        sendAndClear(e.target.value);
    }

});

function sendAndClear(message) {
    if (message !== "") {
        ws.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) {
    let data = JSON.parse(msg.data);
    id("chat").insertAdjacentHTML("beforeend", data.userMessage);
    id("userlist").innerHTML = data.userlist.map(user => "<li>" + user + "</li>").join("");
    window.scrollTo(0,document.body.scrollHeight)
}

function literallyJustToScroll() {
    window.scrollTo(0,document.body.scrollHeight)
}