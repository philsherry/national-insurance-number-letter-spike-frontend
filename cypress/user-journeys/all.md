# User journey: all

This one tries to hit as many pages as possible. To run this: `npm run cypress:open` and choose these files.

- `all.desktop.cy.js`
- `all.tablet.cy.js`
- `all.mobile.cy.js`

1. **Get your National Insurance number by post**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/>
     - Press <kbd>Start now »</kbd>
2. **What is your full name?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-name>
     - Title: `Mister`
     - First name: `John`
     - Middle names: `“Stumpy”`
     - Last name: `Pepys`
     - Press <kbd>Continue</kbd>
3. **Have you had any other names?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-have-a-previous-name>
     - Yes
     - Press <kbd>Continue</kbd>
4. **What is your other name?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/what-is-your-previous-name>
     - First name: `David`
     - Middle names: `Saint`
     - Last name: `Hubbins`
     - Press <kbd>Continue</kbd>
5. **You have added 1 other name**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-have-a-previous-name>
  **Do you want to add another name?**
     - No
     - Press <kbd>Continue</kbd>
6. **What is your date of birth?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-date-of-birth>
     - Day: `11`
     - Month: `1`
     - Year: `1984`
     - Press <kbd>Continue</kbd>
7. **What is your gender?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-gender>
     - Male
     - Press <kbd>Continue</kbd>
8. **What is your telephone number?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-telephone-number>
     - `+44(0)987654321`
     - Press <kbd>Continue</kbd>
9. **Do you know your National Insurance number?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-know-your-national-insurance-number>
     - Yes
     - Press <kbd>Continue</kbd>
10. **What is your National Insurance number?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-national-insurance-number>
     - `AB123456C`
     - Press <kbd>Continue</kbd>
11. **Are you returning to the UK from living abroad?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/are-you-returning-from-living-abroad>
     - Yes
     - Press <kbd>Continue</kbd>
12. **Is your current home address in the UK?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/is-your-current-address-in-uk>
     - Yes
     - Press <kbd>Continue</kbd>
13. **What is your current home address?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-is-your-current-uk-address>
     - Address line 1: `Anfield Road`
     - Town or city: `Liverpool`
     - Postcode: `L4 0TH`
     - Press <kbd>Continue</kbd>
14. **Do you have any previous home addresses?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-have-any-previous-addresses>
     - Yes
     - Press <kbd>Continue</kbd>
15. **Is your previous home address in the UK?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/is-your-previous-address-in-uk>
     - No
     - Press <kbd>Continue</kbd>
16. **What is your previous home address?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/what-is-your-previous-international-address>
     - Address line 1: `Arenaslingan 14`
     - Address line 2: `Johanneshov`
     - Address line 3: `Stockholm`
     - Postcode or zip code: `12177`
     - Country: `Sweden`
     - From: `1` `2010`
     - To: `12` `2020`
     - Press <kbd>Continue</kbd>
17. **You have added 1 previous home address**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-have-any-previous-addresses>
  **Do you want to add another previous home address?**
     - No
     - Press <kbd>Continue</kbd>
18. **Are you married or in a civil partnership?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/are-you-in-a-relationship>
     - No
     - Press <kbd>Continue</kbd>
19. **Have you previously been in a marriage or civil partnership?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/have-you-been-in-a-previous-marriage-or-civil-partnership>
     - Yes
     - Press <kbd>Continue</kbd>
20. **Is this relationship a marriage or civil partnership?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/previous-relationship-type>
     - Marriage
     - Press <kbd>Continue</kbd>
21. **When were you previously married?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/when-were-you-previously-in-a-marriage-or-civil-partnership>

    **Start date**
      - Day: `1`
      - Month: `1`
      - Year: `2021`

    **End date**
      - Day: `31`
      - Month: `12`
      - Year: `2021`

    **Why did the marriage end?**
      - `divorce`
      - Press <kbd>Continue</kbd>
22. **You have added 1 previous relationship**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/have-you-been-in-a-previous-marriage-or-civil-partnership>
  **Do you want to add another previous marriage or civil partnership?**
     - No
     - Press <kbd>Continue</kbd>
23. **Have you ever claimed Child Benefit?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/have-you-ever-claimed-child-benefit>
     - Yes
     - Press <kbd>Continue</kbd>
24. **Do you know your Child Benefit number?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-know-your-child-benefit-number>
     - No
     - Press <kbd>Continue</kbd>
25. **Have you ever received any other UK benefits?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/have-you-ever-received-other-uk-benefits>
     - Yes
     - Press <kbd>Continue</kbd>
26. **What other UK benefits have you received?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/what-other-uk-benefits-have-you-received>
     - `Universal Credit, Tax Credits.`
     - Press <kbd>Continue</kbd>
27. **Have you ever worked in the UK?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/have-you-ever-worked-in-uk>
     - Yes
     - Press <kbd>Continue</kbd>
28. **You have added no employers**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/your-employment-history>
     - Yes
     - Press <kbd>Continue</kbd>
29. **What is your employer’s name?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/what-is-your-employers-name>
     - `EVIL CORP.`
     - Press <kbd>Continue</kbd>
30. **What is EVIL CORP.’s address?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/what-is-your-employers-address>
     - Address line 1: `666 Evil Street`
     - Address line 2: `Cirith Gorgor`
     - Address line 3: `Mordor`
     - Postcode: `SAU R0N`
     - Press <kbd>Continue</kbd>
31. **When did you start working for EVIL CORP.?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/when-did-you-start-working-for-your-employer>
     - Day: `6`
     - Month: `6`
     - Year: `2006`
     - Press <kbd>Continue</kbd>
32. **Are you still employed by EVIL CORP.?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/1/are-you-still-employed>
     - Yes
     - Press <kbd>Continue</kbd>
33. **You have added 1 employer**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/your-employment-history>
  Do you want to add another employer?
     - No
     - Press <kbd>Continue</kbd>
34. **Do you have one of the following identity documents?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/do-you-have-an-identity-document>
     - Yes
     - Press <kbd>Continue</kbd>
35. **Which identity document do you have?**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/which-identity-document-do-you-have>
     - Passport
     - Press <kbd>Continue</kbd>
36. **Check Your Answers**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/check-your-answers>
     - Press <kbd>Continue</kbd>
37. **Your application form is ready to print and post**
  URL: <http://localhost:11300/fill-online/get-your-national-insurance-number-by-post/next-steps>
     1. Download form
     2. “start the application form again”

-- EOL
