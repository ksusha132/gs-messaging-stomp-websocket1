var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/failure', function (fail) {
            showFailures(JSON.parse(fail.body).error);
        });
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/batch', function (batch) {
            showFiles(JSON.parse(batch.body).status);
        });
        stompClient.subscribe('/topic/file', function (batch) {
            showSpecificFiles(JSON.parse(batch.body).status);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function f(z) {

}

const toBase64 = async file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
});

function sendFile() {
    const file = document.querySelector('#file').files[0];
    toBase64(file).then(result => {
        stompClient.send("/app/batch", {}, result.split(',')[1]);
    })
}

function sendSpecFile() {
    const file = document.querySelector('#fileSpec').files[0];
    toBase64(file).then(result => {
        stompClient.send("/app/file", {}, result.split(',')[1]);
    })
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showFailures(message) {
    $("#failure").append("<tr><td>" + message + "</td></tr>");
}

function showFiles(message) {
    $("#upfiles").append("<tr><td>" + message + "</td></tr>");
}

function showSpecificFiles(message) {
    $("#upFilesSpec").append("<tr><td>" + message + "</td></tr>");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
    $("#upload").click(function () {
        sendFile();
    });
    $("#upSpecFiles").click(function () {
        sendSpecFile();
    });
});

