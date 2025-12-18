.global _start

_start:
    MOV R0, #2             // Load R0 with a test value (2 in this case)

    CMP R0, #1             // Check if R0 equals 1
    BEQ case_1             // If equal, branch to case_1

    CMP R0, #2             // Check if R0 equals 2
    BEQ case_2             // If equal, branch to case_2

    CMP R0, #3             // Check if R0 equals 3
    BEQ case_3             // If equal, branch to case_3

    B default_case         // If no case matched, branch to default_case

case_1:
    // Code for case 1
    MOV R1, #10            // Example operation
    B end                  // Go to end

case_2:
    // Code for case 2
    MOV R1, #20            // Example operation
    B end                  // Go to end

case_3:
    // Code for case 3
    MOV R1, #30            // Example operation
    B end                  // Go to end

default_case:
    // Code for default case
    MOV R1, #0             // Example operation for default case

end:
    // End of the program
    SWI 0x11               // Exit system call

