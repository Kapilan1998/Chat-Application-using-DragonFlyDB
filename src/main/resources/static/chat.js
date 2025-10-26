// Global variable to hold the Stomp client connection
var stompClient = null;

// Global variable to hold the username
var username = null;

// Called when the user submits the username form
function connect(event) {
    // Get the entered username and remove any extra spaces
    username = $("#name").val().trim();

    if (username) {
        // Create a SockJS connection to the "/chat-app" endpoint
        var socket = new SockJS('/chat-app');

        // Create a STOMP client over the SockJS connection
        stompClient = Stomp.over(socket);

        // Connect the STOMP client to the server
        stompClient.connect({}, onConnected, onError);
    }
    // Prevent form submission (which reloads the page)
    event.preventDefault();
}

// Called when the STOMP client successfully connects to the server
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell our username to the server  to announce as new user
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({userName: username, messageType: 'JOIN'})
    )

    $("#username-page").addClass('d-none');
    $("#chat-page").removeClass('d-none');
}

// Called when there is an error connecting to the server
function onError(error) {
    console.log('Could not connect to WebSocket server. Please refresh this page to try again!');
}

// Called when the user submits a chat message
function sendMessage(event) {
    var messageContent = $("#message").val().trim();

    // Only send if the message is not empty and client is connected
    if (messageContent && stompClient) {
        // Create the chat message payload
        var chatMessage = {
            userName: username,
            message: messageContent,
            messageType: 'CHAT'     // Mark it as a normal chat message
        };
        // Send the chat message to the server
        stompClient.send("/app/chat.sendChatMessage", {}, JSON.stringify(chatMessage));
        // Clear the message input field
        $("#message").val('');
    }
    // Prevent form submission (which reloads the page)
    event.preventDefault();
}

// Called when a new message is received from the server
function onMessageReceived(payload) {
    // Parse the incoming message payload
    var message = JSON.parse(payload.body);
    // Create a new div element to display the message
    var messageElement = $('<div>');

    // Handle different types of messages (JOIN, LEAVE, CHAT)
    if (message.messageType === 'JOIN') {
        // Format JOIN messages with css styles
        messageElement.addClass('text-center system-message');
        messageElement.text(message.message);
    } else if (message.messageType === 'LEAVE') {
        // Format LEAVE messages with css styles
        messageElement.addClass('text-center system-message system-leave');
        messageElement.text(message.message);
    } else if (message.messageType === 'CHAT') {
        // Format CHAT messages (user chat) with css styles
        messageElement.html('<strong>' + message.userName + ':</strong> ' + message.message);

        // Highlight differently if it’s the message from current user
        if (message.userName === username) {
            messageElement.addClass('user-message');
        } else {
            messageElement.addClass('other-message');
        }
    }

    // Append the message element to the chat area
    $("#chat").append(messageElement);
    // Automatically scroll chat to the bottom to show the latest message
    $("#chat").scrollTop($("#chat")[0].scrollHeight);
}


// DOM Ready: Set up event listeners for the username form and message form
$(document).ready(function () {
    // When user submits username
    $("#usernameForm").on('submit', connect);
    // When user submits a chat message
    $("#messageForm").on('submit', sendMessage);
});
