Si vuole implementare un servizio di FTP usando la libreria New IO. Si deve sviluppare un server che implementi il servizio di FTP ed un client che si connette al server per scaricare uno o più files.

•il client si connette al server mediante un SocketChannel ed invia il nome di un file

•quando il server riceve il nome del file, apre un FileChannel per leggere il file

•quindi registra il canale verso il client con un selettore per inviare il file

•preleva i dati ricevuti dal FileChannel e li invia al client mediante il SocketChannel