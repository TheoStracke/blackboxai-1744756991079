from app import db
from models import Pizza, Extra, Combo

def seed_database():
    # Clear existing data
    Pizza.query.delete()
    Extra.query.delete()
    Combo.query.delete()
    
    # Add Pizzas
    pizzas = [
        Pizza(
            name='Pepperoni',
            description='Massa branca, molho de tomate, queijo mussarela e pepperoni',
            price=45.00,
            image_url='/images/pepperoni.png',
            is_featured=True
        ),
        Pizza(
            name='Queijo',
            description='Massa branca, molho de tomate, queijo mussarela',
            price=40.00,
            image_url='/images/queijo.png',
            is_featured=True
        ),
        Pizza(
            name='Marguerita',
            description='Massa branca, molho de tomate, queijo mussarela, tomates frescos e manjericão',
            price=43.00,
            image_url='/images/marguerita.png',
            is_featured=True
        )
    ]
    
    # Add Extras (Bordas)
    bordas = [
        Extra(name='Cheddar', price=5.00, type='borda'),
        Extra(name='Catupiry', price=5.00, type='borda'),
        Extra(name='Requeijão', price=5.00, type='borda'),
        Extra(name='Chocolate', price=5.00, type='borda')
    ]
    
    # Add Extras (Bebidas)
    bebidas = [
        Extra(name='Coca-Cola', price=15.00, type='bebida'),
        Extra(name='Guaraná', price=12.00, type='bebida'),
        Extra(name='Fanta', price=10.00, type='bebida'),
        Extra(name='Heineken', price=10.00, type='bebida')
    ]
    
    # Add Combos (example combos based on the UI)
    combos = [
        Combo(
            name='Combo Família',
            description='2 pizzas grandes + refrigerante 2L',
            price=89.90,
            items='{"pizzas": 2, "bebida": "Refrigerante 2L"}',
            image_url='/images/combo_familia.png'
        ),
        Combo(
            name='Combo Casal',
            description='1 pizza grande + refrigerante 1L',
            price=59.90,
            items='{"pizzas": 1, "bebida": "Refrigerante 1L"}',
            image_url='/images/combo_casal.png'
        ),
        Combo(
            name='Combo Individual',
            description='1 pizza média + 1 refrigerante 350ml',
            price=45.90,
            items='{"pizzas": 1, "bebida": "Refrigerante 350ml"}',
            image_url='/images/combo_individual.png'
        )
    ]
    
    # Add all items to database
    db.session.add_all(pizzas)
    db.session.add_all(bordas)
    db.session.add_all(bebidas)
    db.session.add_all(combos)
    
    # Commit changes
    db.session.commit()
    print("Database seeded successfully!")

if __name__ == '__main__':
    # Create all tables
    db.create_all()
    # Seed the database
    seed_database()
