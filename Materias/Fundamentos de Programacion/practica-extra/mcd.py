from math import gcd

def mcd(num1, num2):
    mcd = gcd(num1, num2)
    return mcd

def main():
    print (mcd(2,6))
    print (mcd(3,8))
    print (mcd(10,25))

main()