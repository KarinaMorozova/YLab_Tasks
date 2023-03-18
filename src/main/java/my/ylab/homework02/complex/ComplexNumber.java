package my.ylab.homework02.complex;

import java.util.Objects;

public class ComplexNumber {
    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(double real) {
        this.real = real;
        this.imaginary = 0;
    }

    public double getReal() {
        return this.real;
    }

    public double getImaginary() {
        return this.imaginary;
    }

    public ComplexNumber add(ComplexNumber second) {
        // (a + ib) + (c + id) = (a + c) + i(b + d)
        double realSum = this.real + second.real;
        double imaginarySum = this.imaginary + second.imaginary;

        return new ComplexNumber(realSum, imaginarySum);
    }

    public ComplexNumber subtract(ComplexNumber second) {
        // (a + ib) – (c + id) = (a – c) + i(b – d)
        double realSub = this.real - second.real;
        double imaginarySub = this.imaginary - second.imaginary;

        return new ComplexNumber(realSub, imaginarySub);
    }

    public ComplexNumber multiply(ComplexNumber second) {
        // (a + ib) * (c + id) = (ac – bd) + i(ad + bc)
        double realMult = this.real * second.real - this.imaginary * second.imaginary;
        double imaginaryMult = this.real * second.imaginary + this.imaginary * second.real;

        return new ComplexNumber(realMult, imaginaryMult);
    }

    public double modulus() {
        // |a + ib| = Math.sqrt(a * a + b * b)
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    @Override
    public String toString() {
        String result = "";
        if (this.real != 0) {
            result = this.real + "";
        }
        if (this.imaginary != 0) {
            if (this.real != 0) {
                result = result + " + ";
            }

            result = result + this.imaginary + "i";
        }

        if (this.real == 0 && this.imaginary == 0) {
            result = "0";
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComplexNumber)) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(that.real, real) == 0 && Double.compare(that.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}
