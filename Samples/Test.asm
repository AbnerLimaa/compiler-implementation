.data        # Data declaration section
 hello_msg:   .asciiz "Hello World!\n";

    .text

main:                # Start of code section

    la $a0, hello_msg
    li $v0, 4
    syscall

    # Now do a graceful exit
    li $v0, 10
    syscall