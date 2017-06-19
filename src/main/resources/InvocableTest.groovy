/**
 * Author: zehui.lv@dianrong on 6/19/17.
 */
def rule(person) {
    setter(person)
    return person.age > 20
}

def setter(person) {
    person.city = "shit!"
}