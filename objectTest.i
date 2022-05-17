import I.util.regex.*;
Attributes: { speed: int, name: string, regex: Pattern }
pub fn speedUp() => int {
 return ++speed;
}

pub fn toString() => string {
    return speed+" "+name;
}