case PIPE: {
    p = (struct pipecmd *) cmd;

    int pipe_fds[2];
    if (pipe(pipe_fds) == -1) {
        perror("Error en pipe");
        _exit(-1);
    }

    pid_t left_pid = fork();
    if (left_pid == 0) {
        close(pipe_fds[0]);
        dup2(pipe_fds[1], STDOUT_FILENO);
        close(pipe_fds[1]);

        exec_cmd(p->leftcmd);
        _exit(1);
    }

    pid_t right_pid = fork();
    if (right_pid == 0) {
        close(pipe_fds[1]);
        dup2(pipe_fds[0], STDIN_FILENO);
        close(pipe_fds[0]);

        exec_cmd(p->rightcmd);
        _exit(1);
    }

    close(pipe_fds[0]);
    close(pipe_fds[1]);
    waitpid(left_pid, NULL, 0);
    waitpid(right_pid, NULL, 0);
    _exit(0);
}