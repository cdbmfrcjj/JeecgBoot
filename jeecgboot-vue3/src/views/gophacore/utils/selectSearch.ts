export function filterOption(input, option) {
    console.log(input, option);
    return (option.value&&(option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0)) || (option.label&&(option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0 ))|| (option.text&&(option.text.toLowerCase().indexOf(input.toLowerCase()) >= 0)) || (option.title&&(option.title.toLowerCase().indexOf(input.toLowerCase()) >= 0));
}