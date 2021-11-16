#include <stdio.h>

int main() {

    char c;
    int letters[24], i;

    for (i = 0; i < 24; i++) {
      letters[i] = 0;
    }
    while ((c = getchar()) != EOF) {
      if ('A' <= c && c <= 'Z') {
        int num = c - 65;
        letters[num] += 1;
      } else if ('a' <= c && c <= 'z') {
        int num = c - 97;
        letters[num] += 1;
      }
    }

    int highest = 0;
    for (i = 1; i < 24; i++) {
      if (letters[i] > letters[highest]) {
        highest = i;
      }
    }

    printf("The most frequent character is %c and it occurs %d times\n", highest+65, letters[highest]);

    return 0;
}
