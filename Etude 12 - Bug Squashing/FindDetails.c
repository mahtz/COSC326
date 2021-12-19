#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

#define MAX_LENGTH 205
#define MAX_COMMAND_LENGTH 85

/**
 * Program that allows the user to find full client information by searching them up in the client book 
 * with either their first name, last name, email address, or phone number.
 *
 * @author Mathew Shields
 */

typedef struct Client {
    char *firstName;
    char *lastName;
    char *phone;
    char *emailAddress;
} Client;

void *emalloc(size_t s) {  
    void *result = malloc(s);
    if (NULL == result) {
        fprintf(stderr, "Memory allocation failed!\n");
        exit(EXIT_FAILURE);
    }
    return result;
}
void freeAll(Client **clients, int numClients) {
    int i;
    for (i = 0; i < numClients; i++) {
        free(clients[i]->firstName);
        free(clients[i]->lastName);
        free(clients[i]->phone);
        free(clients[i]->emailAddress);
        free(clients[i]);
    }
    free(clients);
}

int fromFirstName(Client **clients, char *firstName, int numClients) {

    int i;
    for (i = 0; i < numClients; i++) {
        if (strncasecmp(clients[i]->firstName, firstName, strlen(firstName)) == 0) {
            return i;
        }
    }
    return -1;  
}
int fromLastName(Client **clients, char *lastName, int numClients) {

    int i;
    for (i = 0; i < numClients; i++) {
        if (strncasecmp(clients[i]->lastName, lastName, strlen(lastName)) == 0) {
            return i;
        }
    }
    return -1;  
}
int fromPhone(Client **clients, char *phone, int numClients) {

    int i;
    for (i = 0; i < numClients; i++) {
        if (strncasecmp(clients[i]->phone, phone, strlen(phone)) == 0) {
            return i;
        }
    }
    return -1;  
}
int fromEmail(Client **clients, char *email, int numClients) {

    int i;
    for (i = 0; i < numClients; i++) {
        if (strncasecmp(clients[i]->emailAddress, email, strlen(email)) == 0) {
            return i;
        }
    }
    return -1;  
}

void printDetails(Client **clients, int index) {

    printf("\nFirst Name: %s\n", clients[index]->firstName);
    printf("Last Name: %s\n", clients[index]->lastName);
    printf("Phone Number: %s\n", clients[index]->phone);
    printf("Email Address: %s\n\n", clients[index]->emailAddress);
}

int main (int argc, char **argv) {

    FILE *file = fopen(argv[1], "r");
    int i = 0, numClients = 0, index;
    char *fnBuffer, *lnBuffer, *phBuffer, *emBuffer;
    char ch, input[MAX_COMMAND_LENGTH], *command, *item, eachClientString[MAX_LENGTH];
    Client **clients, *clientBuffer;

    if (argc != 2) {
        fprintf(stderr, "Invalid number of arguments. This program takes 1 argument, a file containing client data.\n");
        return EXIT_FAILURE;
    }

    if (file == NULL) {
        fprintf(stderr, "Could not read file '%s'.\n", argv[1]);
        return EXIT_FAILURE;
    }
    
    while ((ch = fgetc(file)) != EOF) {
        if (ch == '\n') {
            numClients++;
        }
    }
    clients = emalloc(numClients * sizeof clients[0]);
    rewind(file);

    while (NULL != fgets(eachClientString, MAX_LENGTH, file) && i < numClients) {
        if (eachClientString[strlen(eachClientString) - 1] == '\n') {
            eachClientString[strlen(eachClientString) - 1] = '\0';
        }

        fnBuffer = strtok(eachClientString, " ");
        lnBuffer = strtok(NULL, " ");
        phBuffer = strtok(NULL, " ");
        emBuffer = strtok(NULL, " ");
        
        clientBuffer = emalloc(sizeof *clientBuffer);

        clientBuffer->firstName = emalloc((strlen(fnBuffer) + 1) * sizeof clientBuffer->firstName[0]);
        clientBuffer->lastName = emalloc((strlen(lnBuffer) + 1) * sizeof clientBuffer->lastName[0]);
        clientBuffer->phone = emalloc((strlen(phBuffer) + 1) * sizeof clientBuffer->phone[0]);
        clientBuffer->emailAddress = emalloc((strlen(emBuffer) + 1) * sizeof clientBuffer->emailAddress[0]);

        strcpy(clientBuffer->firstName, fnBuffer);
        strcpy(clientBuffer->lastName, lnBuffer);
        strcpy(clientBuffer->phone, phBuffer);
        strcpy(clientBuffer->emailAddress, emBuffer);

        clients[i] = clientBuffer;     
        i++;
    }
    fclose(file);

    printf("Enter:\n\nf for first name,\nl for last name,\np for phone number,\ne for email address,\n");
    printf("\nfollowed by the respective detail of the client you wish to find, or\nq to quit.\n\n");
    printf("E.g., f <first name>\n\n");

    do {
        printf("--> ");
        
        fgets(input, MAX_COMMAND_LENGTH, stdin);
        command = strtok(input, " ");
        item = strtok(NULL, " ");
        if (item == NULL) {
            if (command[0] != 'q') {
                printf("Enter:\n\nf for first name,\nl for last name,\np for phone number,\ne for email address,\n");
                printf("\nfollowed by the respective detail of the client you wish to find, or\nq to quit.\n\n");
                printf("E.g., f <first name>\n\n");
            }
            continue;
        }

        if (item[strlen(item) - 1] == '\n') {
            item[strlen(item) - 1] = '\0';
        }
        
        switch(command[0]) {
            case 'f':
                index = fromFirstName(clients, item, numClients);
                if (index != -1 ) {
                    printDetails(clients, index);
                } else {
                    printf("\nThere are no clients with the first name %s.\n\n", item);
                }
                break;
            case 'l':
                index = fromLastName(clients, item, numClients);
                if (index != -1 ) {
                    printDetails(clients, index);
                } else {
                    printf("\nThere are no clients with the last name %s.\n\n", item);
                }
                break;
            case 'p':
                index = fromPhone(clients, item, numClients);
                if (index != -1 ) {
                    printDetails(clients, index);
                } else {
                    printf("\nThere are no clients with the phone number %s.\n\n", item);
                }
                break;
            case 'e':
                index = fromEmail(clients, item, numClients);
                if (index != -1 ) {
                    printDetails(clients, index);
                } else {
                    printf("\nThere are no clients with the email address %s.\n\n", item);
                }
                break;
            case 'q':
                break;
            default:
                printf("Enter:\n\nf for first name,\nl for last name,\np for phone number,\ne for email address,\n");
                printf("\nfollowed by the respective detail of the client you wish to find, or\nq to quit.\n\n");
                printf("E.g., f <first name>\n\n");
                break;
        }
        
    } while (command[0] != 'q');

    freeAll(clients, numClients);

    return EXIT_SUCCESS;
}
