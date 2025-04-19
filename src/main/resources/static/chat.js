    var stompClient = null;
    var username = null;

    function connect(event) {
    username = $("#name").val().trim();

    if (username) {
    var socket = new SockJS('/chat-app');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}
    event.preventDefault();
}

    function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
{},
    JSON.stringify({userName: username, messageType: 'JOIN'})
    )

    $("#username-page").addClass('d-none');
    $("#chat-page").removeClass('d-none');
}

    function onError(error) {
    console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
}

    function sendMessage(event) {
    var messageContent = $("#message").val().trim();
    if(messageContent && stompClient) {
    var chatMessage = {
    userName: username,
    message: messageContent,
    messageType: 'CHAT'
};
    stompClient.send("/app/chat.sendChatMessage", {}, JSON.stringify(chatMessage));
    $("#message").val('');
}
    event.preventDefault();
}


    function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = $('<div>');

    if (message.messageType === 'JOIN') {
    messageElement.addClass('text-center system-message');
    messageElement.text(message.message);
} else if (message.messageType === 'LEAVE') {
    messageElement.addClass('text-center system-message system-leave');
    messageElement.text(message.message);
} else if (message.messageType === 'CHAT') {
    messageElement.html('<strong>' + message.userName + ':</strong> ' + message.message);

    // Add class based on whether it's the current user
    if (message.userName === username) {
    messageElement.addClass('user-message');
} else {
    messageElement.addClass('other-message');
}
}

    $("#chat").append(messageElement);
    $("#chat").scrollTop($("#chat")[0].scrollHeight);
}


    $(document).ready(function() {
    $("#usernameForm").on('submit', connect);
    $("#messageForm").on('submit', sendMessage);
});
